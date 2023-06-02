package one.xingyi.optics;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics(debug = true, addListTraversal = true, traversals = {"main2Child:children", "main2Grandchild:children.grandChildren"})
record MainForTest(List<ChildForTest> children, ChildForTest child) {
}

@Optics(addListTraversal = true)
record ChildForTest(List<GrandChildForTest> grandChildren, GrandChildForTest grandChild) {
}

@Optics(addListTraversal = true)
record GrandChildForTest(String name) {
    public GrandChildForTest withName(String name) {
        return new GrandChildForTest(name);
    }
}

@Optics
record State(DataA a, DataB b) {
}

@Optics
record DataA(String a1, String a2) {
}

@Optics
record DataB(String b1, String b2) {
}