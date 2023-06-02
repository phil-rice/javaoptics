package one.xingyi.optics;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ITraversalTest extends OpticsFixture {

    @Test
    public void testTraversals() {
        assertEquals(List.of(childWithOneGrandChild, childWithTwoGrandChildren),
                MainForTestOptics.childrenT.all(mainWithTwoChildren).toList());
    }

    @Test
    public void testLinkedTraversals() {
        assertEquals(List.of(childWithOneGrandChild, childWithTwoGrandChildren),
                MainForTestOptics.main2Child.all(mainWithTwoChildren).toList());

        assertEquals(List.of(grandChild0, grandChild1, grandChild2),
                MainForTestOptics.main2Grandchild.all(mainWithTwoChildren).toList());
    }

    @Test
    public void testTraversalsWithLens() {
        assertEquals(List.of(List.of(grandChild0), List.of(grandChild1, grandChild2)),
                MainForTestOptics.childrenT.<List<GrandChildForTest>>chainTraversal(ChildForTestOptics.grandChildrenL).all(mainWithTwoChildren).toList());
    }

    @Test
    public void testWithFilter() {
        assertEquals(List.of(childWithTwoGrandChildren), MainForTestOptics.childrenT.filter(c -> c.grandChildren().size() > 1).all(mainWithTwoChildren).toList());
    }

    @Test
    public void testModify() {
        assertEquals(new ChildForTest(List.of(new GrandChildForTest("grandChild1x"), new GrandChildForTest("grandChild2x")), grandChild1),
                ChildForTestOptics.grandChildrenT.modify(childWithTwoGrandChildren, gc -> gc.withName(gc.name() + "x")));
        assertEquals(mainWithTwoChildrenAndX, MainForTestOptics.main2Grandchild.modify(mainWithTwoChildren, gc -> new GrandChildForTest(gc.name() + "x")));
    }

    @Test
    public void testListTraversal() {
        assertEquals(List.of(mainWithOneChild, mainWithTwoChildren),
                MainForTestOptics.listT.all(List.of(mainWithOneChild, mainWithTwoChildren)).toList());
    }
}

