package one.xingyi.optics;

import one.xingyi.utils.StreamHelper;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StreamHelperTest {

    @Test
    void testFirst() {
        assertEquals("a", StreamHelper.first(Stream.of("a", "b", "c")));
        assertNull(StreamHelper.first(Stream.of()));
    }

    @Test
    void testLast() {
        assertEquals("c", StreamHelper.last(Stream.of("a", "b", "c")));
        assertNull(StreamHelper.last(Stream.of()));
    }
}
