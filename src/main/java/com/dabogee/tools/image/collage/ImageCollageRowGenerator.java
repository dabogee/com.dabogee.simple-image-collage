package com.dabogee.tools.image.collage;

import com.dabogee.tools.image.collage.effects.BufferedImageBorderEffect;
import com.dabogee.tools.image.collage.effects.BufferedImageRoundCornerEffect;
import com.dabogee.tools.image.collage.effects.BufferedImageScaleEffect;
import com.dabogee.tools.image.collage.models.CollageRow;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.imgscalr.Scalr.Mode.FIT_TO_HEIGHT;
import static org.imgscalr.Scalr.Mode.FIT_TO_WIDTH;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class ImageCollageRowGenerator {

    private final Map<Integer, BufferedImage> images;

    private final CollageRow row;

    public static ImageCollageRowGenerator of(Map<Integer, BufferedImage> images, CollageRow row) {
        checkArgument(nonNull(images), "images can not be null");
        checkArgument(nonNull(row), "row can not be null");
        checkArgument(images.size() > 0, "images can not be empty");
        checkArgument(CollectionUtils.isNotEmpty(row.getIds()), "row can not be empty");
        return new ImageCollageRowGenerator(images, row);
    }

    public BufferedImage concat(ImageCollageProperties properties) {
        Integer minHeight = getMinHeight();
        checkArgument(minHeight < Integer.MAX_VALUE, "unable to detect a min height");

        List<BufferedImage> resizeOnHeight = new ArrayList<>();

        for (Integer imageId : row.getIds()) {
            BufferedImage resized = BufferedImageScaleEffect
                    .of(images.get(imageId), FIT_TO_HEIGHT, minHeight)
                    .apply();

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

//        double ratio = properties.getMaxWidth() / width.doubleValue();
//        List<BufferedImage> modifiedByEffects = new ArrayList<>();
//
//        for (BufferedImage image : resizeOnHeight) {
//            /*
//             * Fit to width
//             */
//            BufferedImage resized =
//                    BufferedImageScaleEffect
//                    .of(image, FIT_TO_WIDTH, (int) (image.getWidth() * ratio))
//                    .apply();
//
//
//
//            modifiedByEffects.add(bordered);
//
//            /*
//             * Corner
//             */
////            BufferedImage cornered =
////                    BufferedImageRoundCornerEffect
////                            .of(bordered, properties.getCornerRadius())
////                            .apply();
//
////            modifiedByEffects.add(cornered);
//        }


        BufferedImage result = new BufferedImage(width, minHeight, BufferedImage.TYPE_INT_RGB);
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

    Integer getMinHeight() {
        int minHeight = Integer.MAX_VALUE;
        for (Integer id : row.getIds()) {
            checkArgument(
                    images.containsKey(id),
                    "id=%d there is no image with such id", id
            );

            if (images.get(id).getHeight() < minHeight) {
                minHeight = images.get(id).getHeight();
            }
        }

        return minHeight;
    }

    /**
     * Total length of a row in pixels based on sum.
     */
    Integer getWidth() {
        int width = 0;
        for (Integer id : row.getIds()) {
            checkArgument(
                    images.containsKey(id),
                    "id=%d there is no image with such id", id
            );

            width += images.get(id).getWidth();
        }

        return width;
    }
}
