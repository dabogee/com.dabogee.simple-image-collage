package com.dabogee.tools.image.collage.models;

import com.dabogee.tools.image.collage.ImageCollageProperties;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollageRow {

    @Getter
    public final Map<Integer, CollageImageMeta> images = new HashMap<>();

    public void put(BufferedImage image) {
        images.put(image.hashCode(), CollageImageMeta.of(image));
    }

    public Set<Integer> getIds() {
        return images.keySet();
    }

    public CollageRow transform(ImageCollageProperties properties) {
        Integer minHeight = getMinHeight();
        if (minHeight == Integer.MAX_VALUE) {
            return this;
        }
        for (CollageImageMeta image : images.values()) {
            image.fitToHeight(minHeight);
        }

        Double ratio = properties.getMaxWidth() / getTotalWidth().doubleValue();
        images.values().forEach(i -> i.scale(ratio));

        /*
         * Fix height for all images after scaling.
         */
        minHeight = getMinHeight();
        for (CollageImageMeta image : images.values()) {
            image.setHeight(minHeight);
        }

        /*
         * Fix total width.
         */
        int diff = properties.getMaxWidth() - getTotalWidth();
        images.values().stream().findAny().ifPresent(i -> i.addWidth(diff));

        return this;
    }

    public Integer getTotalWidth() {
        return images.values().stream()
                .map(CollageImageMeta::getWidth)
                .reduce(0, Integer::sum);
    }

    public Integer getMinHeight() {
        if (images.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        return images.values().stream()
                .map(CollageImageMeta::getHeight)
                .min(Integer::compareTo)
                .get();
    }
}
