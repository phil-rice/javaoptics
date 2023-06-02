package one.xingyi.optics;

import one.xingyi.tuples.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ILensTest extends OpticsFixture {

    @Test
    public void testSetAndGet() {
        assertEquals(List.of(childWithNoGrandChildren), MainForTestOptics.childrenL.get(mainWithOneChild));
        assertEquals(new MainForTest(List.of(), childWithNoGrandChildren), MainForTestOptics.childrenL.set(mainWithOneChild, List.of()));
    }

    @Test
    public void testSetAndGetWhenChained() {
        assertEquals(grandChild0, MainForTestOptics.childL.chainLens(ChildForTestOptics.grandChildL).get(mainWithTwoChildren));
        assertEquals(new MainForTest(List.of(childWithNoGrandChildren), new ChildForTest(List.of(), grandChild0)), MainForTestOptics.childL.chainLens(ChildForTestOptics.grandChildL).set(mainWithOneChild, grandChild0));
    }

    @Test
    public void testMerge() {
        var s = new State(new DataA("a1", "a2"), new DataB("b1", "b2"));
        var s2 = StateOptics.aL.chainLens(DataAOptics.a1L)
                .merge(StateOptics.bL.chainLens(DataBOptics.b1L), IISO.identity()).set(s, new Tuple2<>("newA1", "newB1"));
        assertEquals(new State(new DataA("newA1", "a2"), new DataB("newB1", "b2")), s2);
    }
}
