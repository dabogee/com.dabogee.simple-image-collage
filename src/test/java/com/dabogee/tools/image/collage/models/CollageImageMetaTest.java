package com.dabogee.tools.image.collage.models;

import com.dabogee.tools.image.collage.ImageTestProvider;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CollageImageMetaTest {

    @Test
    void testRead() {
        assertThat(
                ImageTestProvider.get().stream()
                        .map(CollageImageMeta::of)
                        .collect(Collectors.toList())
        ).allSatisfy(i -> {
            assertThat(i.getWidth()).isPositive();
            assertThat(i.getHeight()).isPositive();
        });
    }
}
