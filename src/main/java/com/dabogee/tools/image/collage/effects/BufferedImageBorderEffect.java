package com.dabogee.tools.image.collage.effects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkArgument;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.util.Objects.nonNull;
import static org.imgscalr.Scalr.Mode.FIT_EXACT;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BufferedImageBorderEffect implements BufferedImageEffect {

    private final BufferedImage image;

    private final Integer borderWidth;

    private final Color color;

    public static BufferedImageBorderEffect of(BufferedImage image,
                                               Integer borderWidth,
                                               Color borderColor) {
        checkArgument(nonNull(image), "image can not be null");
        checkArgument(nonNull(borderColor), "color can not be null");
        checkArgument(borderWidth >= 0, "ratio should be a positive number");
        return new BufferedImageBorderEffect(image, borderWidth, borderColor);
    }

    @Override
    public BufferedImage apply() {
        if (borderWidth < 1) {
            return image;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics2D graphics = result.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);

        graphics.drawImage(
                Scalr.resize(
                        image,
                        Scalr.Method.QUALITY,
                        FIT_EXACT,
                        width - 2 * borderWidth,
                        height - 2 * borderWidth
                ),
                borderWidth,
                borderWidth,
                null
        );
        graphics.dispose();
        return result;
    }
}
