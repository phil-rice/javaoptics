package one.xingyi.fp;

import lombok.var;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class IPartialFunctionTest {


    Predicate<String> isHello = s -> s.equals("hello");
    Predicate<String> hasO = s -> s.contains("o");
    Predicate<String> hasE = s -> s.contains("e");
    Predicate<String> isGoodbye = s -> s.equals("goodbye");
    Function<String, String> upperCase = String::toUpperCase;
    PartialFunction<String, String> helloPf = (PartialFunction<String, String>) IPartialFunction.of(isHello, upperCase);
    PartialFunction<String, String> hasOPf = (PartialFunction<String, String>) IPartialFunction.of(hasO, s -> s + " contains o");
    PartialFunction<String, String> hasEPf = (PartialFunction<String, String>) IPartialFunction.of(hasE, s -> s + " contains e");
    PartialFunction<String, String> goodbyePf = (PartialFunction<String, String>) IPartialFunction.of(isGoodbye, upperCase);
    @Test
    void testOf() {
        assertSame(isHello, helloPf.isDefinedAt);
        assertSame(upperCase, helloPf.fn);
    }

    @Test
    void testAlways() {
        Function<String, String> upperCase = String::toUpperCase;
        PartialFunction<String, String> pf = (PartialFunction<String, String>) IPartialFunction.always(upperCase);
        assertTrue(pf.isDefinedAt.test("hello"));
        assertTrue(pf.isDefinedAt.test("anything"));
        assertTrue(pf.isDefinedAt.test(null));
        assertSame(upperCase, pf.fn);
    }

    @Test
    void testNotNull() {
        Function<String, String> upperCase = String::toUpperCase;
        PartialFunction<String, String> pf = (PartialFunction<String, String>) IPartialFunction.notNull(upperCase);
        assertTrue(pf.isDefinedAt.test("hello"));
        assertFalse(pf.isDefinedAt.test(null));
        assertSame(upperCase, pf.fn);
    }
    @Test
    void testApplyOrError() throws Exception {
        assertEquals("HELLO", IPartialFunction.applyOrError(helloPf, "hello"));
        assertThrows(IllegalArgumentException.class, () -> IPartialFunction.applyOrError(helloPf, "not hello"));
    }
    @Test
    void testApplyOr() throws Exception {
        assertEquals("HELLO", IPartialFunction.applyOr(helloPf, () -> "defValue", "hello"));
        assertEquals("defValue", IPartialFunction.applyOr(helloPf, () -> "defValue", "not hello"));
    }

    @Test
    void testMapFn() {
        var pf = IPartialFunction.mapFn(Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf));
        assertEquals(Arrays.asList("HELLO", "hello contains e", "hello contains o"), pf.apply("hello"));
        assertEquals(Arrays.asList("GOODBYE", "goodbye contains e", "goodbye contains o"), pf.apply("goodbye"));
    }
    @Test
    void testMapReduceFn() {
        var pf = IPartialFunction.mapReduceFn(Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf), list -> String.join(":", list));
        assertEquals("HELLO:hello contains e:hello contains o", pf.apply("hello"));
        assertEquals("GOODBYE:goodbye contains e:goodbye contains o", pf.apply("goodbye"));
    }

    @Test
    void testChain() {
        var chained = IPartialFunction.chain("DefValue", Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf));
        assertEquals("HELLO", chained.apply("hello"));
        assertEquals("GOODBYE", chained.apply("goodbye"));
        assertEquals("has o contains o", chained.apply("has o"));
        assertEquals("has e contains e", chained.apply("has e"));
        assertEquals("DefValue", chained.apply("withOut O Or E"));
    }
    @Test
    void testChainPf() {
        var chained = IPartialFunction.chainToPf(Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf));
        assertTrue(chained.isDefinedAt("hello"));
        assertTrue(chained.isDefinedAt("goodbye"));
        assertTrue(chained.isDefinedAt("has o"));
        assertTrue(chained.isDefinedAt("has e"));
        assertFalse(chained.isDefinedAt("withOut O Or E"));

        assertEquals("HELLO", chained.apply("hello"));
        assertEquals("GOODBYE", chained.apply("goodbye"));
        assertEquals("has o contains o", chained.apply("has o"));
        assertEquals("has e contains e", chained.apply("has e"));
    }

}