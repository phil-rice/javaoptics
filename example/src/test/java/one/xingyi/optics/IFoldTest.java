package one.xingyi.optics;

import one.xingyi.utils.StreamHelper;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    void testLastN() {
        assertEquals(List.of("b", "c"), listFold.lastN(2).all(List.of("a", "b", "c")).toList());
        assertEquals(List.of(), listFold.lastN(2).all(List.of()).toList());
        assertEquals(List.of("a"), listFold.lastN(2).all(List.of("a")).toList());
    }
    @Test
    void testMap() {
        assertEquals(List.of("a1", "b1", "c1"), listFold.map(s -> s + "1").all(List.of("a", "b", "c")).toList());
    }

    @Test
    void testOfWithoutNulls() {
        assertEquals(List.of("1", "2", "3"), IFold.ofWithoutNulls(main -> Stream.of(null, "1", null, "2", null, "3")).all("anything").toList());
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
