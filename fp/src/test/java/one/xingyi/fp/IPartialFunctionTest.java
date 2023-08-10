package one.xingyi.fp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.var;
import one.xingyi.helpers.Permutations;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class IPartialFunctionTest {


    Predicate<String> isHello = s -> s.equals("hello");
    Predicate<String> hasO = s -> s.contains("o");
    Predicate<String> hasE = s -> s.contains("e");
    Predicate<String> isGoodbye = s -> s.equals("goodbye");
    Function<String, String> upperCase = String::toUpperCase;
    IPartialFunction<String, String> always = IPartialFunction.always(s -> s + "_always");
    PartialFunction<String, String> helloPf = (PartialFunction<String, String>) IPartialFunction.of(isHello, upperCase);
    PartialFunction<String, String> hasOPf = (PartialFunction<String, String>) IPartialFunction.of(hasO, s -> s + " contains o");
    PartialFunction<String, String> hasEPf = (PartialFunction<String, String>) IPartialFunction.of(hasE, s -> s + " contains e");
    PartialFunction<String, String> goodbyePf = (PartialFunction<String, String>) IPartialFunction.of(isGoodbye, upperCase);
    @Test
    void testOf() {
        assertSame(isHello, helloPf.isDefinedAt);
        assertSame(upperCase, helloPf.fn);
        assertFalse(isHello instanceof PartialFunctionAlwaysTrue);
        assertFalse(upperCase instanceof PartialFunctionAlwaysTrue);
    }

    @Test
    void testAlways() {
        Function<String, String> upperCase = String::toUpperCase;
        PartialFunction<String, String> pf = (PartialFunction<String, String>) IPartialFunction.always(upperCase);
        assertTrue(pf.isDefinedAt.test("hello"));
        assertTrue(pf.isDefinedAt.test("anything"));
        assertTrue(pf.isDefinedAt.test(null));
        assertSame(upperCase, pf.fn);
        assertTrue(pf instanceof PartialFunctionAlwaysTrue);
    }

    @Test
    void testNotNull() {
        Function<String, String> upperCase = String::toUpperCase;
        PartialFunction<String, String> pf = (PartialFunction<String, String>) IPartialFunction.notNull(upperCase);
        assertTrue(pf.isDefinedAt.test("hello"));
        assertFalse(pf.isDefinedAt.test(null));
        assertSame(upperCase, pf.fn);
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    static class ForTest {
        final String a;
        final List<Integer> ns;
    }
    static Function<List<Integer>, Integer> sum = ns -> ns.stream().reduce(0, Integer::sum);
    static Function<ForTest, String> childAFn = ft -> ft.a;
    @Test
    void testFromChild() {
        PartialFunction<ForTest, String> pf = IPartialFunction.fromChild(ForTest::getA, hasO, upperCase);
        assertTrue(pf.isDefinedAt.test(new ForTest("hello", null)));
        assertFalse(pf.isDefinedAt.test(new ForTest("hell", null)));
        assertEquals("HELLO", pf.apply(new ForTest("hello", null)));
    }
    @Test
    void testChildNotNull() {
        PartialFunction<ForTest, String> pf = IPartialFunction.childNotNull(ForTest::getA, upperCase);
        assertTrue(pf.isDefinedAt.test(new ForTest("hello", null)));
        assertFalse(pf.isDefinedAt.test(new ForTest(null, null)));
        assertEquals("HELLO", pf.apply(new ForTest("hello", null)));
    }
    @Test
    void testListDefined() {
        IPartialFunction<List<Integer>, Integer> pf = IPartialFunction.listDefined(sum);
        assertTrue(pf.isDefinedAt(Arrays.asList(1, 2, 3)));
        assertFalse(pf.isDefinedAt(Arrays.asList()));
        assertFalse(pf.isDefinedAt(null));
        assertEquals(6, pf.apply(Arrays.asList(1, 2, 3)));
    }
    @Test
    void testChildListDefined() {
        PartialFunction<ForTest, Integer> pf = IPartialFunction.childListDefined(ForTest::getNs, sum);
        assertTrue(pf.isDefinedAt(new ForTest("hello", Arrays.asList(1, 2, 3))));
        assertFalse(pf.isDefinedAt(new ForTest("hello", Arrays.asList())));
        assertFalse(pf.isDefinedAt(new ForTest("hello", null)));
        assertEquals(6, pf.apply(new ForTest("hello", Arrays.asList(1, 2, 3))));
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

    @Test
    void isOkToUseListBooleans() {
        List<IPartialFunction<String, String>> pfns = Arrays.asList(helloPf, always, goodbyePf);
        assertEquals(true, IPartialFunction.isOkToUseBooleans(pfns).test(Arrays.asList(false, true, false)));
        assertEquals(true, IPartialFunction.isOkToUseBooleans(pfns).test(Arrays.asList(true, true, true)));

        assertEquals(false, IPartialFunction.isOkToUseBooleans(pfns).test(Arrays.asList(false, false, false)));
    }

    @Test
    void testApplyListBooleans() {
        List<IPartialFunction<String, String>> pfns = Arrays.asList(helloPf, always, hasOPf);
        assertEquals(Arrays.asList("f_always"), IPartialFunction.applyListBooleans(pfns, "f").apply(Arrays.asList(false, true, false)));
        assertEquals(Arrays.asList("HELLO", "hello_always", "hello contains o"), IPartialFunction.applyListBooleans(pfns, "hello").apply(Arrays.asList(true, true, true)));
    }
    @Test
    void testPermutations() {
        List<IPartialFunction<String, String>> pfns = Arrays.asList(helloPf, always, hasOPf);
        assertEquals(Arrays.asList(
                Arrays.asList("hello_always"),
                Arrays.asList("HELLO", "hello_always"),
                Arrays.asList("hello_always", "hello contains o"),
                Arrays.asList("HELLO", "hello_always", "hello contains o")
        ), IPartialFunction.permutations(pfns, "hello").collect(Collectors.toList()));
    }
    @Test
    void testforEachPermutation() {
        List<IPartialFunction<String, String>> pfns = Arrays.asList(helloPf, always, hasOPf);
        List<String> results = new ArrayList<>();
        IPartialFunction.forEachPermutation(pfns, "hello", (booleans, merged) -> results.add(booleans + "->" + merged));
        assertEquals(Arrays.asList(
                "[false, true, false]->[hello_always]",
                "[true, true, false]->[HELLO, hello_always]",
                "[false, true, true]->[hello_always, hello contains o]",
                "[true, true, true]->[HELLO, hello_always, hello contains o]"
        ), results);

    }

}