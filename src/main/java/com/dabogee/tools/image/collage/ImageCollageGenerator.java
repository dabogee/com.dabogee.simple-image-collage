package com.dabogee.tools.image.collage;

import com.dabogee.tools.image.collage.models.CollageInstance;
import com.dabogee.tools.image.collage.models.CollageRow;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageCollageGenerator {

    @Getter(AccessLevel.PACKAGE)
    private final Map<Integer, BufferedImage> images;
    private final ImageCollageProperties properties;

    public static ImageCollageGenerator files(List<File> files, ImageCollageProperties properties) throws IOException {
        checkArgument(CollectionUtils.isNotEmpty(files), "set of files is null or empty");
        Map<Integer, BufferedImage> images = new HashMap<>();
        for (File f : files) {
            BufferedImage image = ImageIO.read(f);
            images.put(image.hashCode(), image);
        }
        return new ImageCollageGenerator(images, properties);
    }

    public static ImageCollageGenerator images(List<BufferedImage> images, ImageCollageProperties properties) {
        checkArgument(CollectionUtils.isNotEmpty(images), "set of images is null or empty");
        return new ImageCollageGenerator(
                images.stream().collect(Collectors.toMap(BufferedImage::hashCode, Function.identity())),
                properties
        );
    }

    public static ImageCollageGenerator is(List<InputStream> iss, ImageCollageProperties properties) throws IOException {
        checkArgument(CollectionUtils.isNotEmpty(iss), "set of iss is null or empty");
        List<BufferedImage> images = new ArrayList<>();
        for (InputStream is : iss) {
            images.add(ImageIO.read(is));
        }
        return images(images, properties);
    }

    public BufferedImage concat() {
        CollageInstance collage = prepare();
        List<BufferedImage> rows = new ArrayList<>();

        for (CollageRow row : collage.getRows()) {
            rows.add(
                    ImageCollageRowGenerator
                            .of(images, row)
                            .concat(properties)
            );
        }

        Integer height =
                rows.stream()
                        .map(BufferedImage::getHeight)
                        .reduce(0, Integer::sum);
        int top = 0;

        BufferedImage result =
                new BufferedImage(
                        properties.getMaxWidth(),
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D graphics = result.createGraphics();

        for (BufferedImage current : rows) {
            graphics.drawImage(current, 0, top, null);
            top += current.getHeight();
        }

        graphics.dispose();
        return result;
    }

    /**
     * Organize a collage row-by-row.
     */
    CollageInstance prepare() {
        CollageInstance result = new CollageInstance();
        CollageInstance backup = new CollageInstance();

        for (int i = 0; i < properties.getGenerateAttempts(); i++) {
            CollageInstance candidate = generate();
            double ratioDiff = candidate.getWidthToHeightRatio() - properties.getMinRatio();
            double resultDiff = result.getWidthToHeightRatio() - properties.getMinRatio();

            if (ratioDiff > 0 && ratioDiff < resultDiff) {
                result = candidate;
            }

            if (candidate.getWidthToHeightRatio() < properties.getMaxRatio()) {
                if (backup.isEmpty()) {
                    backup = candidate;
                }
                else if (candidate.getWidthToHeightRatio() > backup.getWidthToHeightRatio()) {
                    backup = candidate;
                }
            }
        }

        return result.isEmpty() ?
                backup.isEmpty() ? generate() : backup :
                result;
    }

    private CollageInstance generate() {
        CollageInstance collage = new CollageInstance();
        Set<Integer> usedIds = collage.getUsedIds();    // optimization

        while (usedIds.size() + 1 < images.size() &&
                collage.getRowsCount() <= properties.getMaxRows()) {
            Integer randomImagesInRowCount = properties.getRandomCount();
            CollageRow row = new CollageRow();

            /*
             * Put the widest image into row with single image.
             */
            if (randomImagesInRowCount == 1) {
                Optional<BufferedImage> imageOpt = getWidestImage(usedIds);
                if (imageOpt.isEmpty()) {
                    break;
                }
                row.put(imageOpt.get());
            }
            else {
                for (int i = 0; i < randomImagesInRowCount; i++) {
                    Optional<Integer> imageIdOpt = getRandomImageId(usedIds);
                    if (imageIdOpt.isEmpty()) {
                        break;
                    }
                    row.put(images.get(imageIdOpt.get()));
                    usedIds.add(imageIdOpt.get());
                }
            }

            if (row.getImages().isEmpty()) {
                break;
            }

            collage.add(row.transform(properties));
            usedIds = collage.getUsedIds();
        }

        return collage;
    }

    Optional<Integer> getRandomImageId(Set<Integer> excludedIds) {
        List<Integer> ids = new ArrayList<>(images.keySet());
        if (nonNull(excludedIds)) {
            ids.removeAll(excludedIds);
        }
        if (ids.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ids.get(RandomUtils.nextInt(0, ids.size())));
    }

    /**
     * Is used to pick up the widest image to fill
     * a row with only one image.
     */
    Optional<BufferedImage> getWidestImage(Set<Integer> excludedIds) {
        Optional<BufferedImage> candidateOpt = Optional.empty();

        for (Map.Entry<Integer, BufferedImage> entry : images.entrySet()) {
            if (nonNull(excludedIds) && excludedIds.contains(entry.getKey())) {
                continue;
            }
            if (candidateOpt.isEmpty()) {
                candidateOpt = Optional.of(entry.getValue());
                continue;
            }

            BufferedImage candidate = candidateOpt.get();
            if (entry.getValue().getWidth() > candidate.getWidth()) {
                candidateOpt = Optional.of(entry.getValue());
            }
        }

        return candidateOpt;
    }
}
