package com.dabogee.tools.image.collage.models;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class CollageRow {

    @Getter
    public final Set<Integer> ids = new HashSet<>();

    @Getter
    @Setter
    private BufferedImage image;

    public void put(Integer imageId) {
        ids.add(imageId);
    }
}
