package com.dabogee.tools.image.collage;

import com.dabogee.tools.image.collage.models.CollageInstance;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCollageRowGeneratorTest {

    @Test
    void testConcat() throws IOException {
        ImageCollageProperties properties =
                ImageCollageProperties.builder().build();

        ImageCollageGenerator generator =
                ImageCollageGenerator.is(ImageTestProvider.stream(), properties);

        CollageInstance collageInstance = generator.prepare();

        ImageCollageRowGenerator rowGenerator =
                ImageCollageRowGenerator.of(
                        generator.getImages(),
                        collageInstance.getRows().get(1)
                );

        BufferedImage result = rowGenerator.concat(properties);

        ImageIO.write(
                result,
                "jpg",
                new File("/Users/dabogee/src/row-concat.jpg")
        );
    }
}
