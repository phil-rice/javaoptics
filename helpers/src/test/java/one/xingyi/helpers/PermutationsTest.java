package one.xingyi.helpers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PermutationsTest {

    @Test
    void testToList() {
        assertEquals(Arrays.asList(false, false, false), Permutations.toList(0, 3));
        assertEquals(Arrays.asList(true, false, false), Permutations.toList(1, 3));
        assertEquals(Arrays.asList(false, true, false), Permutations.toList(2, 3));
        assertEquals(Arrays.asList(true, true, false), Permutations.toList(3, 3));
        assertEquals(Arrays.asList(false, false, true), Permutations.toList(4, 3));
        assertEquals(Arrays.asList(true, false, true), Permutations.toList(5, 3));
        assertEquals(Arrays.asList(false, true, true), Permutations.toList(6, 3));
        assertEquals(Arrays.asList(true, true, true), Permutations.toList(7, 3));
    }

    @Test
    void testPermutate() {
        assertEquals(Arrays.asList(
                Arrays.asList(false, false, false),
                Arrays.asList(true, false, false),
                Arrays.asList(false, true, false),
                Arrays.asList(true, true, false),
                Arrays.asList(false, false, true),
                Arrays.asList(true, false, true),
                Arrays.asList(false, true, true),
                Arrays.asList(true, true, true)), Permutations.permutate(3).collect(Collectors.toList()));
    }

}