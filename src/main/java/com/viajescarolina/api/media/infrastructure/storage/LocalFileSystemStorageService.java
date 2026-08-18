package com.viajescarolina.api.media.infrastructure.storage;

import com.viajescarolina.api.media.domain.MediaStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

@ApplicationScoped
public class LocalFileSystemStorageService implements MediaStorageService {

    private static final Logger LOG = Logger.getLogger(LocalFileSystemStorageService.class);

    @ConfigProperty(name = "viajescarolina.media.storage-dir", defaultValue = "../media")
    String storageDirPath;

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
        Path baseDir = getStoragePath();
        String safeName = originalFilename != null ? originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_") : "asset.webp";
        String uniqueFilename = UUID.randomUUID().toString().substring(0, 8) + "-" + safeName;
        Path targetFile = baseDir.resolve(uniqueFilename);

        int width = 0;
        int height = 0;
        long actualSize = 0;

        try {
            // Buffer to calculate dimensions and save
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            byte[] fileBytes = baos.toByteArray();
            actualSize = fileBytes.length;

            // Extract image dimensions if image
            if (mimeType != null && mimeType.startsWith("image/")) {
                try {
                    BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(fileBytes));
                    if (bimg != null) {
                        width = bimg.getWidth();
                        height = bimg.getHeight();
                    }
                } catch (Exception ex) {
                    LOG.warn("No se pudieron extraer dimensiones de imagen para " + originalFilename, ex);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(targetFile.toFile())) {
                fos.write(fileBytes);
            }

        } catch (IOException e) {
            LOG.error("Error al guardar archivo en disco: " + targetFile, e);
            throw new RuntimeException("Error al almacenar activo multimedia: " + e.getMessage(), e);
        }

        String storagePath = "/media/" + uniqueFilename;
        String variantsJson = String.format("{\"original\": \"%s\"}", storagePath);

        return new StoredFileInfo(uniqueFilename, storagePath, actualSize, width, height, variantsJson);
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
