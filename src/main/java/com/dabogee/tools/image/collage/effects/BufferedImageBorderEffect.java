package com.dabogee.tools.image.collage.effects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

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

        Graphics2D graphics = image.createGraphics();
        int width = image.getWidth();
        int height = image.getHeight();
        graphics.setColor(color);

        graphics.fillRect(0, 0, width, borderWidth);
        graphics.fillRect(0, 0, borderWidth, height);
        graphics.fillRect(width - borderWidth, 0, borderWidth, height);
        graphics.fillRect(0, height - borderWidth, width, height - borderWidth);
        graphics.dispose();
        return image;
    }
}
