package com.dabogee.tools.image.collage.models;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

@Data
@Accessors(chain = true)
public class CollageImageMeta {

    private Integer width;

    private Integer height;

    public static CollageImageMeta of(InputStream is) {
        checkArgument(nonNull(is), "input stream is null");
        try {
            return of(ImageIO.read(is));
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static CollageImageMeta of(BufferedImage image) {
        checkArgument(nonNull(image), "image is null");
        checkArgument(image.getWidth() > 0, "width should be positive");
        checkArgument(image.getHeight() > 0, "height should be positive");
        return new CollageImageMeta()
                .setWidth(image.getWidth())
                .setHeight(image.getHeight());
    }
}
