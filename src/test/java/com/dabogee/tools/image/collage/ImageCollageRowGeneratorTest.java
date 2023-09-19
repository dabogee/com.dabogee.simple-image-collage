package com.dabogee.tools.image.collage;

import com.dabogee.tools.image.collage.models.CollageInstance;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageCollageRowGeneratorTest {

    @Test
    void testConcat() throws IOException {
        ImageCollageProperties properties =
                ImageCollageProperties.builder().build();

        ImageCollageGenerator generator =
                ImageCollageGenerator.is(ImageTestProvider.get(), properties);

        CollageInstance collageInstance = generator.prepare();

        ImageCollageRowGenerator rowGenerator =
                ImageCollageRowGenerator.of(
                        generator.getImages(),
                        collageInstance.getRows().get(1)
                );

        BufferedImage result = rowGenerator.concat(properties);

//        ImageIO.write(
//                result,
//                "jpg",
//                new File("row-concat.jpg")
//        );
    }
}
