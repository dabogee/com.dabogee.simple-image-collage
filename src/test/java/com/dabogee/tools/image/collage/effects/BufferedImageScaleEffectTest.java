package com.dabogee.tools.image.collage.effects;

import com.dabogee.tools.image.collage.ImageTestProvider;
import org.imgscalr.Scalr;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BufferedImageScaleEffectTest {

    @Test
    void testApply() throws IOException {
        BufferedImage originImage = ImageIO.read(ImageTestProvider.get().get(2));

        BufferedImage modifiedImage =
                BufferedImageScaleEffect
                        .of(originImage, Scalr.Mode.FIT_TO_HEIGHT, 300)
                        .apply();

//        ImageIO.write(
//                modifiedImage,
//                "jpg",
//                new File("02-scaled-height.jpg")
//        );

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(modifiedImage, "jpg", os);
        assertThat(os.size()).isPositive();
    }
}
