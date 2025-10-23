package ua.everybuy.service.photo;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.errorhandling.custom.FileFormatException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;


/**
 * Service for rotating images by 90-degree increments.
 * Supports common image formats: PNG, JPG, JPEG, BMP, GIF.
 * Rotation is performed in multiples of 90 degrees (0, 90, 180, 270).
 */
@Service
public class ImageRotationService {
    private static final Set<String> SUPPORTED_FORMATS = Set.of("png", "jpg", "jpeg", "bmp", "gif");

    public byte[] rotateImage(MultipartFile photo, int rotation) throws IOException {
        String fileFormat = getFormatName(photo);
        BufferedImage originalImage = ImageIO.read(photo.getInputStream());
        BufferedImage rotatedImage = rotateImage(originalImage, rotation);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(rotatedImage, fileFormat, os);
        return os.toByteArray();
    }

    public String getFormatName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new FileFormatException("Unable to determine image format from filename");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1)
                .toLowerCase();

        if (!SUPPORTED_FORMATS.contains(ext)) {
            throw new FileFormatException("Unsupported image format: " + ext);
        }

        return "jpeg".equals(ext) ? "jpg" : ext;
    }

    /**
     * Performs the actual image rotation using affine transformations.
     *
     * @param originalImage the source image to rotate
     * @param rotation      the number of 90-degree increments (0-3)
     * @return the rotated BufferedImage
     */
    private BufferedImage rotateImage(BufferedImage originalImage, int rotation) {
        if (rotation < 0 || rotation > 3) return originalImage;

        double radians = Math.toRadians(rotation * 90);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int newWidth = (rotation % 2 == 0) ? width : height;
        int newHeight = (rotation % 2 == 0) ? height : width;

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = rotated.createGraphics();
        g2d.rotate(radians, newWidth / 2.0, newHeight / 2.0);
        g2d.drawImage(originalImage, (newWidth - width) / 2, (newHeight - height) / 2, null);
        g2d.dispose();

        return rotated;
    }

}
