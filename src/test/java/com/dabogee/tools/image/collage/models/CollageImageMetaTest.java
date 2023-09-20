package com.dabogee.tools.image.collage.models;

import com.dabogee.tools.image.collage.ImageTestProvider;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CollageImageMetaTest {

    @Test
    void testRead() {
        assertThat(
                ImageTestProvider.stream().stream()
                        .map(CollageImageMeta::of)
                        .collect(Collectors.toList())
        ).allSatisfy(i -> {
            assertThat(i.getWidth()).isPositive();
            assertThat(i.getHeight()).isPositive();
        });
    }

    @Test
    void testFitToHeight() {
        CollageImageMeta meta = new CollageImageMeta().setWidth(640).setHeight(480);
        CollageImageMeta result = meta.fitToHeight(200);
        assertThat(result.getHeight()).isEqualTo(200);
        assertThat(result.getWidth()).isEqualTo(266);
    }
}
