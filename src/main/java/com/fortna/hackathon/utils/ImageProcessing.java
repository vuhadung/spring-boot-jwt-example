package com.fortna.hackathon.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageProcessing {

    private static final Logger logger = LoggerFactory.getLogger(ImageProcessing.class);

    public static String compressAvatar(byte[] avatar) {
        try {
            Image rawImage = ImageIO.read(new ByteArrayInputStream(avatar));

            Image resultingImage = rawImage.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "png", output);

            return Base64.encodeBase64String(output.toByteArray());
        } catch (IOException e) {
            logger.error("Error while compressing avatar: {}", e.getMessage());
        }
        return null;
    }

}
