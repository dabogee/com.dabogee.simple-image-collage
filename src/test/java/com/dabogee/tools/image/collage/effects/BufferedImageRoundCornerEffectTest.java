package com.dabogee.tools.image.collage.effects;

import com.dabogee.tools.image.collage.ImageTestProvider;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BufferedImageRoundCornerEffectTest {

    @Test
    void testApply() throws IOException {
        BufferedImage originImage = ImageIO.read(ImageTestProvider.stream().get(0));

        BufferedImage modifiedImage =
                BufferedImageRoundCornerEffect
                        .of(originImage, 20, Color.WHITE)
                        .apply();

        ImageIO.write(
                modifiedImage,
                "png",
                new File("/Users/dabogee/src/01-rounded.png")
        );

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(modifiedImage, "png", os);
        assertThat(os.size()).isPositive();
    }
}
