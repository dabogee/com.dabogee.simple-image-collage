package com.dabogee.tools.image.collage.effects;

import com.dabogee.tools.image.collage.models.CollageImageMeta;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BufferedImageRoundCornerEffect implements BufferedImageEffect {

    private final BufferedImage image;
    private final Integer radius;

    public static BufferedImageRoundCornerEffect of(BufferedImage image, Integer radius) {
        checkArgument(radius > 0, "radius should be a positive number");
        CollageImageMeta meta = CollageImageMeta.of(image);
        checkArgument(
                Math.min(meta.getWidth(), meta.getHeight()) >= radius,
                "radius is too high"
        );
        return new BufferedImageRoundCornerEffect(image, radius);
    }

    @Override
    public BufferedImage apply() {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage output =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );

        Graphics2D g2 = output.createGraphics();

        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);

        g2.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        g2.drawImage(image, 0, 0, null);

        g2.dispose();
        return output;
    }
}
