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

    /**
     * width / height.
     * Avoiding horizontal grey borders (Telegram).
     */
    @Builder.Default
    private Double maxRatio = 5.0;

    /**
     * width / height.
     * Avoiding vertical grey borders (Telegram).
     */
    @Builder.Default
    private Double minRatio = 1.0;

    /**
     * Number of attempts to put into ratio range.
     */
    @Builder.Default
    private Integer generateAttempts = 20;

    @Builder.Default
    private Integer borderWidth = 1;

    @Builder.Default
    private Color borderColor = Color.WHITE;

    /**
     * Does not look good with this effect on small sizes.
     */
    @Builder.Default
    private Integer cornerRadius = 0;

    @Builder.Default
    private Integer minImagesPerRow = 1;

    @Builder.Default
    private Integer maxImagesPerRow = 4;

    @Builder.Default
    private Integer maxRows = 5;

    public Integer getRandomCount() {
        return RandomUtils.nextInt(minImagesPerRow, maxImagesPerRow + 1);
    }
}
