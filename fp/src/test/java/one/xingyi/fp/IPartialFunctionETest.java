package one.xingyi.fp;

import lombok.var;
import one.xingyi.interfaces.FunctionWithException;
import one.xingyi.interfaces.PredicateWithException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class IPartialFunctionETest {


    PredicateWithException<String> isHello = s -> s.equals("hello");
    PredicateWithException<String> hasO = s -> s.contains("o");
    PredicateWithException<String> hasE = s -> s.contains("e");
    PredicateWithException<String> isGoodbye = s -> s.equals("goodbye");
    FunctionWithException<String, String> upperCase = String::toUpperCase;
    PartialFunctionE<String, String> helloPf = (PartialFunctionE<String, String>) IPartialFunctionE.of(isHello, upperCase);
    PartialFunctionE<String, String> hasOPf = (PartialFunctionE<String, String>) IPartialFunctionE.of(hasO, s -> s + " contains o");
    PartialFunctionE<String, String> hasEPf = (PartialFunctionE<String, String>) IPartialFunctionE.of(hasE, s -> s + " contains e");
    PartialFunctionE<String, String> goodbyePf = (PartialFunctionE<String, String>) IPartialFunctionE.of(isGoodbye, upperCase);
    @Test
    void testOf() {
        assertSame(isHello, helloPf.isDefinedAt);
        assertSame(upperCase, helloPf.fn);
    }

    @Test
    void testAlways() throws Exception {
        FunctionWithException<String, String> upperCase = String::toUpperCase;
        PartialFunctionE<String, String> pf = (PartialFunctionE<String, String>) IPartialFunctionE.always(upperCase);
        assertTrue(pf.isDefinedAt.test("hello"));
        assertTrue(pf.isDefinedAt.test("anything"));
        assertTrue(pf.isDefinedAt.test(null));
        assertSame(upperCase, pf.fn);
    }

    @Test
    void testNotNull() throws Exception {
        FunctionWithException<String, String> upperCase = String::toUpperCase;
        PartialFunctionE<String, String> pf = (PartialFunctionE<String, String>) IPartialFunctionE.notNull(upperCase);
        assertTrue(pf.isDefinedAt.test("hello"));
        assertFalse(pf.isDefinedAt.test(null));
        assertSame(upperCase, pf.fn);
    }
    @Test
    void testApplyOrError() throws Exception {
        assertEquals("HELLO", IPartialFunctionE.applyOrError(helloPf, "hello"));
        assertThrows(IllegalArgumentException.class, () -> IPartialFunctionE.applyOrError(helloPf, "not hello"));
    }
    @Test
    void testApplyOr() throws Exception {
        assertEquals("HELLO", IPartialFunctionE.applyOr(helloPf, () -> "defValue", "hello"));
        assertEquals("defValue", IPartialFunctionE.applyOr(helloPf, () -> "defValue", "not hello"));
    }

    @Test
    void testMapFn() throws Exception {
        var pf = IPartialFunctionE.mapFn(Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf));
        assertEquals(Arrays.asList("HELLO", "hello contains e", "hello contains o"), pf.apply("hello"));
        assertEquals(Arrays.asList("GOODBYE", "goodbye contains e", "goodbye contains o"), pf.apply("goodbye"));
    }
    @Test
    void testMapReduceFn() throws Exception {
        var pf = IPartialFunctionE.mapReduceFn(Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf), list -> String.join(":", list));
        assertEquals("HELLO:hello contains e:hello contains o", pf.apply("hello"));
        assertEquals("GOODBYE:goodbye contains e:goodbye contains o", pf.apply("goodbye"));
    }

    @Test
    void testChain() throws Exception {
        var chained = IPartialFunctionE.chain("DefValue", Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf));
        assertEquals("HELLO", chained.apply("hello"));
        assertEquals("GOODBYE", chained.apply("goodbye"));
        assertEquals("has o contains o", chained.apply("has o"));
        assertEquals("has e contains e", chained.apply("has e"));
        assertEquals("DefValue", chained.apply("withOut O Or E"));
    }
    @Test
    void testChainPf() throws Exception {
        var chained = IPartialFunctionE.chainToPfE(Arrays.asList(helloPf, goodbyePf, hasEPf, hasOPf));
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