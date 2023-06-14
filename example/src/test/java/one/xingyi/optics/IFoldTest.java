package one.xingyi.optics;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IFoldTest {
    IFold<List<List<String>>, List<String>> listsFold = IFold.of(Collection::stream);
    IFold<List<String>, String> listFold = IFold.of(Collection::stream);
    @Test
    void testMakeFold() {
        assertEquals(List.of("a", "b", "c"), listFold.all(List.of("a", "b", "c")).toList());
    }

    @Test
    void testChainFold() {
        var f = listsFold.chainFold(listFold);
        assertEquals(List.of("a", "b", "c"), f.all(List.of(List.of("a", "b"), List.of("c"))).toList());
    }
    @Test
    void testFilter() {
        assertEquals(List.of("a", "c"), listFold.all(List.of("a", "b", "c")).filter(s -> s.equals("a") || s.equals("c")).toList());
    }
    @Test
    void testMap() {
        assertEquals(List.of("a1", "b1", "c1"), listFold.map(s -> s + "1").all(List.of("a", "b", "c")).toList());
    }
    @Test
    void testForEach() throws Exception {
        var result = new StringBuilder();
        listFold.forEach(List.of("a", "b", "c"), result::append);
        assertEquals("abc", result.toString());
    }
    @Test
    void unique() {
        assertEquals(List.of("a", "b", "c"), listFold.unique().all(List.of("a", "b", "c", "a", "b", "c")).toList());
    }

}
