package com.dabogee.tools.image.collage;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTestProvider {

    private static final String DIR = "rooms/";
    private static final List<String> FILENAMES =
            List.of(
                    "01.jpeg", "02.jpeg", "03.jpeg", "04.jpeg",
                    "05.jpeg", "06.jpeg", "07.jpeg", "08.jpeg",
                    "09.jpeg", "10.jpeg", "11.jpeg", "12.jpeg"
            );

    public static List<InputStream> stream() {
        ClassLoader cl = ImageTestProvider.class.getClassLoader();
        return FILENAMES.stream()
                .map(fn -> cl.getResourceAsStream(DIR + fn))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public static List<BufferedImage> images() {
        List<BufferedImage> result = new ArrayList<>();
        for (InputStream is : stream()) {
            result.add(ImageIO.read(is));
        }
        return result;
    }

    public static InputStream stream(String filename) {
        return ImageTestProvider.class.getClassLoader().getResourceAsStream(DIR + filename);
    }

    public static void main(String[] args) {
        assertThat(ImageTestProvider.stream())
                .isNotEmpty()
                .allSatisfy(is -> assertThat(is).isNotEmpty());
    }
}
