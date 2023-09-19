package com.dabogee.tools.image.collage.effects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BufferedImageScaleEffect implements BufferedImageEffect {

    private final BufferedImage image;

    private final Scalr.Mode scalingMode;
    private final Integer targetSize;

    public static BufferedImageScaleEffect of(BufferedImage image,
                                              Scalr.Mode mode,
                                              Integer targetSize) {
        checkArgument(nonNull(image), "image can not be null");
        checkArgument(nonNull(mode), "scaling mode can not be null");
        checkArgument(targetSize > 0, "ratio should be a positive number");
        return new BufferedImageScaleEffect(image, mode, targetSize);
    }

    @Override
    public BufferedImage apply() {
        return Scalr.resize(image, scalingMode, targetSize);
    }
}
