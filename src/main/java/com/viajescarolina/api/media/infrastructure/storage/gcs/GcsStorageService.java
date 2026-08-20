package com.viajescarolina.api.media.infrastructure.storage.gcs;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.viajescarolina.api.media.domain.MediaStorageService;
import com.viajescarolina.api.media.infrastructure.imaging.ImageOptimizer;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Almacenamiento en Google Cloud Storage — usado únicamente en producción (perfil %prod).
 * En desarrollo/pruebas locales se usa {@link com.viajescarolina.api.media.infrastructure.storage.LocalFileSystemStorageService}
 * (disco local, dentro del propio proyecto), seleccionado automáticamente por Quarkus
 * cuando este bean no está activo (ver @DefaultBean en esa clase).
 *
 * Requiere que el bucket exista y tenga acceso público de lectura configurado a nivel de
 * bucket (uniform bucket-level access + IAM allUsers:objectViewer) — esta clase no gestiona
 * permisos, solo sube/lee/borra objetos. Las credenciales se resuelven automáticamente vía
 * Application Default Credentials (Workload Identity en Cloud Run, o GOOGLE_APPLICATION_CREDENTIALS).
 */
@ApplicationScoped
@IfBuildProfile("prod")
public class GcsStorageService implements MediaStorageService {

    private static final Logger LOG = Logger.getLogger(GcsStorageService.class);

    @ConfigProperty(name = "viajescarolina.media.gcs-bucket")
    String bucketName;

    @ConfigProperty(name = "viajescarolina.media.gcs-public-base-url")
    java.util.Optional<String> publicBaseUrl;

    @Inject
    ImageOptimizer imageOptimizer;

    private volatile Storage storage;

    private Storage storageClient() {
        Storage local = storage;
        if (local == null) {
            synchronized (this) {
                if (storage == null) {
                    storage = StorageOptions.getDefaultInstance().getService();
                }
                local = storage;
            }
        }
        return local;
    }

    @Override
    public StoredFileInfo store(String originalFilename, String mimeType, InputStream inputStream, long sizeBytes) {
        byte[] rawBytes;
        try {
            rawBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo subido: " + e.getMessage(), e);
        }

        ImageOptimizer.OptimizedImage optimized = imageOptimizer.optimize(rawBytes, mimeType);

        String safeBaseName = (originalFilename != null ? originalFilename : "asset")
                .replaceAll("\\.[^.]+$", "")
                .replaceAll("[^a-zA-Z0-9.-]", "_");
        String objectName = "media/" + UUID.randomUUID().toString().substring(0, 8) + "-" + safeBaseName + "." + optimized.extension();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(optimized.mimeType())
                .setCacheControl("public, max-age=31536000, immutable")
                .build();

        storageClient().create(blobInfo, optimized.bytes());

        String storagePath = publicBaseUrl.map(base -> base.replaceAll("/$", "") + "/" + objectName)
                .orElse("https://storage.googleapis.com/" + bucketName + "/" + objectName);
        String variantsJson = String.format("{\"original\": \"%s\"}", storagePath);

        return new StoredFileInfo(objectName, storagePath, optimized.bytes().length, optimized.width(), optimized.height(), variantsJson);
    }

    @Override
    public InputStream retrieve(String filename) {
        Blob blob = storageClient().get(BlobId.of(bucketName, filename));
        if (blob == null || !blob.exists()) {
            throw new RuntimeException("Archivo no encontrado en GCS: " + filename);
        }
        return new ByteArrayInputStream(blob.getContent());
    }

    @Override
    public void delete(String filename) {
        try {
            storageClient().delete(BlobId.of(bucketName, filename));
        } catch (Exception e) {
            LOG.warn("No se pudo eliminar el objeto en GCS: " + filename, e);
        }
    }
}
