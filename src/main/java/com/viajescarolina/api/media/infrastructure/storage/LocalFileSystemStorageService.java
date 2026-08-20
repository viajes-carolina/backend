package com.viajescarolina.api.media.infrastructure.storage;

import com.viajescarolina.api.media.domain.MediaStorageService;
import com.viajescarolina.api.media.infrastructure.imaging.ImageOptimizer;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Almacenamiento local en disco — usado en desarrollo y en las pruebas del propio
 * proyecto. En producción se reemplaza por {@link com.viajescarolina.api.media.infrastructure.storage.gcs.GcsStorageService}
 * (perfil %prod), ver ese bean para el almacenamiento en Google Cloud Storage.
 */
@ApplicationScoped
@DefaultBean
public class LocalFileSystemStorageService implements MediaStorageService {

    private static final Logger LOG = Logger.getLogger(LocalFileSystemStorageService.class);

    @ConfigProperty(name = "viajescarolina.media.storage-dir", defaultValue = "../media")
    String storageDirPath;

    @Inject
    ImageOptimizer imageOptimizer;

    private Path getStoragePath() {
        Path path = Paths.get(storageDirPath).toAbsolutePath().normalize();
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            LOG.error("Error al crear directorio de almacenamiento multimedia: " + path, e);
        }
        return path;
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

        Path baseDir = getStoragePath();
        String safeBaseName = (originalFilename != null ? originalFilename : "asset")
                .replaceAll("\\.[^.]+$", "")
                .replaceAll("[^a-zA-Z0-9.-]", "_");
        String uniqueFilename = UUID.randomUUID().toString().substring(0, 8) + "-" + safeBaseName + "." + optimized.extension();
        Path targetFile = baseDir.resolve(uniqueFilename);

        try (FileOutputStream fos = new FileOutputStream(targetFile.toFile())) {
            fos.write(optimized.bytes());
        } catch (IOException e) {
            LOG.error("Error al guardar archivo en disco: " + targetFile, e);
            throw new RuntimeException("Error al almacenar activo multimedia: " + e.getMessage(), e);
        }

        String storagePath = "/media/" + uniqueFilename;
        String variantsJson = String.format("{\"original\": \"%s\"}", storagePath);

        return new StoredFileInfo(uniqueFilename, storagePath, optimized.bytes().length, optimized.width(), optimized.height(), variantsJson);
    }

    @Override
    public InputStream retrieve(String filename) {
        Path filePath = getStoragePath().resolve(filename);
        File file = filePath.toFile();
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("Archivo no encontrado: " + filename);
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo no encontrado: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path filePath = getStoragePath().resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            LOG.warn("No se pudo eliminar el archivo físico: " + filename, e);
        }
    }
}
