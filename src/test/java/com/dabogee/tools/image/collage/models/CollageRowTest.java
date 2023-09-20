package com.dabogee.tools.image.collage.models;

import com.dabogee.tools.image.collage.ImageCollageProperties;
import com.dabogee.tools.image.collage.ImageTestProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CollageRowTest {

    @Test
    void testGetScaledImages() throws IOException {
        CollageRow row = new CollageRow();
        ImageTestProvider.images().subList(1, 5).forEach(row::put);

        ImageCollageProperties props = ImageCollageProperties.builder().build();
        CollageRow result = row.transform(props);
        Integer minHeight = result.getMinHeight();

        assertThat(result.getTotalWidth()).isEqualTo(props.getMaxWidth());
        assertThat(result.getImages().values()).allSatisfy(i -> {
            assertThat(i.getHeight()).isEqualTo(minHeight);
        });
    }
}
