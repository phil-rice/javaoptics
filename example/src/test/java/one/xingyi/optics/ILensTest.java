package one.xingyi.optics;

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
}
