package com.dabogee.tools.image.collage.effects;

import com.dabogee.tools.image.collage.ImageTestProvider;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BufferedImageBorderEffectTest {

    @Test
    void testApply() throws IOException {
        BufferedImage originImage = ImageIO.read(ImageTestProvider.stream().get(0));

        BufferedImage modifiedImage =
                BufferedImageBorderEffect
                        .of(originImage, 5, Color.WHITE)
                        .apply();

//        ImageIO.write(
//                modifiedImage,
//                "png",
//                new File("01-bordered.png")
//        );

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(modifiedImage, "jpg", os);
        assertThat(os.size()).isPositive();
    }
}
