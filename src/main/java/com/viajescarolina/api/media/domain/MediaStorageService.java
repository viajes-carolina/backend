package com.viajescarolina.api.media.domain;

import java.io.InputStream;

public interface MediaStorageService {

    record StoredFileInfo(String filename, String storagePath, long fileSizeBytes, int width, int height, String variantsJson) {}

    StoredFileInfo store(String originalFilename, String mimeType, InputStream inputStream, long sizeBytes);

    /**
     * Almacena el archivo tal cual, sin pasar por el pipeline de optimización/validación de
     * imágenes ({@code ImageOptimizer}). Pensado para consumidores fuera de la librería de
     * medios que necesitan persistir archivos no-imagen (p. ej. adjuntos PDF del Libro de
     * Reclamaciones, BC claims) reutilizando el mismo backend de almacenamiento activo
     * (disco local en dev, GCS en prod) sin que ImageOptimizer los rechace por no decodificar
     * como imagen.
     */
    StoredFileInfo storeRaw(String originalFilename, String mimeType, InputStream inputStream, long sizeBytes);

    InputStream retrieve(String filename);

    void delete(String filename);
}
