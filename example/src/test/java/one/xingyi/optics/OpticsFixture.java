package one.xingyi.optics;

import java.util.List;

abstract class OpticsFixture {
    protected final GrandChildForTest grandChild0 = new GrandChildForTest("grandChild0");
    protected final GrandChildForTest grandChild1 = new GrandChildForTest("grandChild1");
    protected final GrandChildForTest grandChild2 = new GrandChildForTest("grandChild2");
    protected final GrandChildForTest grandChildNew = new GrandChildForTest("grandChildNew");
    protected final ChildForTest childWithNoGrandChildren = new ChildForTest(List.of(), null);
    protected final ChildForTest childWithOneGrandChild = new ChildForTest(List.of(grandChild0), grandChild0);
    protected final ChildForTest childWithTwoGrandChildren = new ChildForTest(List.of(grandChild1, grandChild2), grandChild1);
    protected final MainForTest mainWithNoChildren = new MainForTest(List.of(), null);
    protected final MainForTest mainWithOneChild = new MainForTest(List.of(childWithNoGrandChildren), childWithNoGrandChildren);
    protected final MainForTest mainWithTwoChildren = new MainForTest(List.of(childWithOneGrandChild, childWithTwoGrandChildren), childWithOneGrandChild);
    protected final MainForTest mainWithTwoChildrenAndX = new MainForTest(List.of(
            new ChildForTest(List.of(new GrandChildForTest("grandChild0x")), grandChild0),
            new ChildForTest(List.of(new GrandChildForTest("grandChild1x"), new GrandChildForTest("grandChild2x")), grandChild1)), childWithOneGrandChild);

}
