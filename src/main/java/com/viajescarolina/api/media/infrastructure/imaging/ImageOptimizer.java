package com.viajescarolina.api.media.infrastructure.imaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

/**
 * Redimensiona y recomprime imágenes subidas para reducir su peso sin pérdida perceptible
 * de calidad, usando únicamente javax.imageio (sin códecs nativos: seguro para desplegar
 * en Cloud Run sin depender de bibliotecas del sistema operativo del contenedor).
 *
 * También sirve como el único punto de validación de "magic bytes": si el contenido no
 * decodifica como una imagen real, se rechaza aquí (ver hallazgo de auditoría SEC-018).
 */
@ApplicationScoped
public class ImageOptimizer {

    private static final Logger LOG = Logger.getLogger(ImageOptimizer.class);

    private static final int MAX_DIMENSION_PX = 2400;
    private static final float JPEG_QUALITY = 0.82f;

    public record OptimizedImage(byte[] bytes, String mimeType, String extension, int width, int height) {}

    /**
     * @throws IllegalArgumentException si el contenido no es una imagen válida.
     */
    public OptimizedImage optimize(byte[] original, String declaredMimeType) {
        BufferedImage source;
        try {
            source = ImageIO.read(new ByteArrayInputStream(original));
        } catch (Exception e) {
            throw new IllegalArgumentException("El archivo no es una imagen válida", e);
        }
        if (source == null) {
            throw new IllegalArgumentException("El archivo no es una imagen válida");
        }

        int originalWidth = source.getWidth();
        int originalHeight = source.getHeight();

        // Los GIF pueden ser animados; recodificar solo el primer frame los rompería.
        // Se preservan tal cual, solo validados.
        if ("image/gif".equalsIgnoreCase(declaredMimeType)) {
            return new OptimizedImage(original, "image/gif", "gif", originalWidth, originalHeight);
        }

        BufferedImage resized = resizeIfNeeded(source, MAX_DIMENSION_PX);
        boolean hasAlpha = resized.getColorModel().hasAlpha();

        byte[] recompressed;
        String mimeType;
        String extension;
        try {
            if (hasAlpha) {
                recompressed = encodePng(resized);
                mimeType = "image/png";
                extension = "png";
            } else {
                recompressed = encodeJpeg(resized, JPEG_QUALITY);
                mimeType = "image/jpeg";
                extension = "jpg";
            }
        } catch (Exception e) {
            LOG.warn("No se pudo recomprimir la imagen, se conserva el archivo original", e);
            return new OptimizedImage(original, declaredMimeType, extensionFor(declaredMimeType), originalWidth, originalHeight);
        }

        // Nunca entregar un archivo más pesado que el original.
        if (recompressed.length >= original.length && resized.getWidth() == originalWidth) {
            return new OptimizedImage(original, declaredMimeType, extensionFor(declaredMimeType), originalWidth, originalHeight);
        }

        return new OptimizedImage(recompressed, mimeType, extension, resized.getWidth(), resized.getHeight());
    }

    private BufferedImage resizeIfNeeded(BufferedImage source, int maxDimension) {
        int width = source.getWidth();
        int height = source.getHeight();
        int longEdge = Math.max(width, height);
        if (longEdge <= maxDimension) {
            return source;
        }

        double scale = maxDimension / (double) longEdge;
        int newWidth = (int) Math.round(width * scale);
        int newHeight = (int) Math.round(height * scale);

        int imageType = source.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage resized = new BufferedImage(newWidth, newHeight, imageType);
        Graphics2D g = resized.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(source, 0, 0, newWidth, newHeight, null);
        } finally {
            g.dispose();
        }
        return resized;
    }

    private byte[] encodeJpeg(BufferedImage image, float quality) throws Exception {
        // JPEG no soporta transparencia; convertir a RGB opaco por si acaso.
        BufferedImage rgb = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        try {
            g.drawImage(image, 0, 0, null);
        } finally {
            g.dispose();
        }

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No hay un ImageWriter de JPEG disponible");
        }
        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (MemoryCacheImageOutputStream ios = new MemoryCacheImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(rgb, null, null), param);
        } finally {
            writer.dispose();
        }
        return baos.toByteArray();
    }

    private byte[] encodePng(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    private String extensionFor(String mimeType) {
        if (mimeType == null) return "webp";
        return switch (mimeType.toLowerCase()) {
            case "image/png" -> "png";
            case "image/jpeg" -> "jpg";
            case "image/gif" -> "gif";
            default -> "webp";
        };
    }
}
