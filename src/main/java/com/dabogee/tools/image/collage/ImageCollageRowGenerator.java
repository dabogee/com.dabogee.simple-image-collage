package com.dabogee.tools.image.collage;

import com.dabogee.tools.image.collage.effects.BufferedImageBorderEffect;
import com.dabogee.tools.image.collage.effects.BufferedImageRoundCornerEffect;
import com.dabogee.tools.image.collage.effects.BufferedImageScaleEffect;
import com.dabogee.tools.image.collage.models.CollageImageMeta;
import com.dabogee.tools.image.collage.models.CollageRow;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.imgscalr.Scalr.Mode.FIT_EXACT;
import static org.imgscalr.Scalr.Mode.FIT_TO_WIDTH;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class ImageCollageRowGenerator {

    private final Map<Integer, BufferedImage> images;

    private final CollageRow row;

    public static ImageCollageRowGenerator of(Map<Integer, BufferedImage> images, CollageRow row) {
        checkArgument(nonNull(images), "images can not be null");
        checkArgument(nonNull(row), "row can not be null");
        checkArgument(images.size() > 0, "images can not be empty");
        checkArgument(!row.getImages().isEmpty(), "row can not be empty");
        return new ImageCollageRowGenerator(images, row);
    }

    public BufferedImage concat(ImageCollageProperties properties) {
        List<BufferedImage> resizeOnHeight = new ArrayList<>();

        for (Map.Entry<Integer, CollageImageMeta> entry : row.getImages().entrySet()) {
            BufferedImage resized =
                    Scalr.resize(
                            images.get(entry.getKey()),
                            Scalr.Method.QUALITY,
                            FIT_EXACT,
                            entry.getValue().getWidth(),
                            entry.getValue().getHeight()
                    );

            /*
             * Corner
             */
            BufferedImage cornered =
                    BufferedImageRoundCornerEffect
                            .of(resized, properties.getCornerRadius())
                            .apply();

            /*
             * Border
             */
            BufferedImage bordered =
                    BufferedImageBorderEffect
                            .of(cornered, properties.getBorderWidth(), properties.getBorderColor())
                            .apply();


            resizeOnHeight.add(bordered);
        }

        Integer width =
                resizeOnHeight.stream()
                        .map(BufferedImage::getWidth)
                        .reduce(0, Integer::sum);
        int left = 0;

        BufferedImage result = new BufferedImage(width, row.getMinHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = result.createGraphics();

        for (BufferedImage current : resizeOnHeight) {
            graphics.drawImage(current, left, 0, null);
            left += current.getWidth();
        }

        graphics.dispose();

        return BufferedImageScaleEffect
                .of(result, FIT_TO_WIDTH, properties.getMaxWidth())
                .apply();
    }
}
