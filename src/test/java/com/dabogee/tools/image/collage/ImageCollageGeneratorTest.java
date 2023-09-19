package com.dabogee.tools.image.collage;

import com.dabogee.tools.image.collage.models.CollageInstance;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageCollageGeneratorTest {

    @Test
    void testInit() throws IOException {
        ImageCollageGenerator generator =
                ImageCollageGenerator.is(
                        ImageTestProvider.get(),
                        ImageCollageProperties.builder().build()
                );

        assertThat(generator.getImages()).hasSameSizeAs(ImageTestProvider.get());

        Optional<BufferedImage> candidateOpt = generator.getWidestImage(new HashSet<>());
        assertThat(candidateOpt).isPresent();

        Optional<Integer> randomImageId =
                generator.getRandomImageId(Set.of(1944201789, 842179210, 1899223686));

        assertThat(randomImageId).isPresent();
    }

    @Test
    void testPrepare() throws IOException {
        ImageCollageGenerator generator =
                ImageCollageGenerator.is(
                        ImageTestProvider.get(),
                        ImageCollageProperties.builder().build()
                );

        for (int i = 0; i < 1000; i++) {
            CollageInstance collageInstance = generator.prepare();
            assertThat(collageInstance.getRowsCount()).isPositive();
        }
    }

    @Test
    void testGenerate() throws IOException {
        ImageCollageGenerator generator =
                ImageCollageGenerator.is(
                        ImageTestProvider.get(),
                        ImageCollageProperties.builder().build()
                );

//        ImageIO.write(
//                generator.concat(),
//                "jpg",
//                new File("collage-concat.jpg")
//        );
    }
}
