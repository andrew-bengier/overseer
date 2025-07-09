package com.bnfd.overseer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageAssetService {
    // region - Class Variables -
    @Value("${spring.application.assetDirectory:/overseer/assets/}")
    private String assetDirectory;

    private final ResourceLoader resourceLoader;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public ImageAssetService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    // endregion - Constructors -

    public void testOverlay(boolean centerOverlay) throws IOException {
        //FileInputStream input = new FileInputStream(DEFAULT_SETTINGS_FILE)
        Resource posterResource = resourceLoader.getResource("classpath:" + "poster-test.jpeg");
        Resource overlayResource = resourceLoader.getResource("file:" + assetDirectory + "archive-overlay-white.png");
        BufferedImage poster = ImageIO.read(posterResource.getInputStream());
        BufferedImage overlay = ImageIO.read(overlayResource.getInputStream());

        int width = poster.getWidth();
        int height = poster.getHeight();

        int resizeX = overlay.getWidth() * 2;
        int resizeY = overlay.getHeight() * 2;

        int centerX = (width - resizeX) / 2;
        int centerY = (height - resizeY) / 2;

        BufferedImage compositeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = compositeImage.createGraphics();
        g2d.drawImage(poster, 0, 0, null);

        g2d.setColor(new Color(20, 20, 20, 154));
        g2d.fillRect(0, 0, width, height);

        float alpha = 1f; // Set overlay transparency (0.5f for overlay that is not as bright)
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(alphaComposite);

        StringBuilder outputName = new StringBuilder();
        outputName.append("poster");
        if (centerOverlay) {
            g2d.drawImage(overlay, centerX, centerY, resizeX, resizeY, null);
            outputName.append("-centered.jpg");
        } else {
            g2d.drawImage(overlay, 0, 0, resizeX, resizeY, null);
            outputName.append("-default.jpg");
        }

        g2d.dispose();
        ImageIO.write(compositeImage, "png", new File(assetDirectory + outputName.toString()));
    }
}
