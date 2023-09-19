package com.dabogee.tools.image.collage;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.awt.*;

@Builder
@Getter
public class ImageCollageProperties {

    @Builder.Default
    private Integer maxWidth = 600;

    @Builder.Default
    private Integer borderWidth = 3;

    @Builder.Default
    private Color borderColor = Color.WHITE;

    @Builder.Default
    private Integer cornerRadius = 10;

    @Builder.Default
    private Integer minImagesPerRow = 2;

    @Builder.Default
    private Integer maxImagesPerRow = 5;

    @Builder.Default
    private Integer maxRows = 10;

    public Integer getRandomCount() {
        return RandomUtils.nextInt(minImagesPerRow, maxImagesPerRow + 1);
    }
}
