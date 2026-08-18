package com.viajescarolina.api.media.domain;

import java.io.InputStream;

public interface MediaStorageService {

    record StoredFileInfo(String filename, String storagePath, long fileSizeBytes, int width, int height, String variantsJson) {}

    StoredFileInfo store(String originalFilename, String mimeType, InputStream inputStream, long sizeBytes);

    InputStream retrieve(String filename);

    void delete(String filename);
}
