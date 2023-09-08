package one.xingyi.profile.pathmap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IPathMapTest {

    @Test
    void testEmpty() {
        IPathMap<String> pathMap = IPathMap.make(() -> "root");
        assertEquals("root", pathMap.get());
        assertEquals("", pathMap.path());
        assertEquals(0, pathMap.childPaths().size());
    }

    @Test
    void testChildDoesntAffectRoot() {
        IPathMap<String> pathMap = IPathMap.make(() -> "default");
        IPathMap<String> child = pathMap.child("child1");
        pathMap.put("rootValue");
        assertEquals("rootValue", pathMap.get());
        assertEquals("default", child.get());
        assertEquals("", pathMap.path());
        assertEquals(1, pathMap.childPaths().size());
        assertEquals("child1", pathMap.childPaths().iterator().next());
    }
    @Test
    void testCanSetChild() {
        IPathMap<String> pathMap = IPathMap.make(() -> "default");
        IPathMap<String> child = pathMap.child("child");
        child.put("childValue");
        assertEquals("default", pathMap.get());
        assertEquals("childValue", child.get());
        assertEquals("", pathMap.path());
        assertEquals(1, pathMap.childPaths().size());
        assertEquals("child", pathMap.childPaths().iterator().next());
        assertEquals("child", child.path());
    }

    @Test
    void testCanSetGrandChild() {
        IPathMap<String> pathMap = IPathMap.make(() -> "default");
        IPathMap<String> child = pathMap.child("child");
        IPathMap<String> grandChild = child.child("grandChild");
        grandChild.put("grandChildValue");
        assertEquals("default", pathMap.get());
        assertEquals("default", child.get());
        assertEquals("grandChildValue", grandChild.get());
        assertEquals("", pathMap.path());
        assertEquals(1, pathMap.childPaths().size());
        assertEquals("child", pathMap.childPaths().iterator().next());
        assertEquals("child", child.path());
        assertEquals(1, child.childPaths().size());
        assertEquals("grandChild", child.childPaths().iterator().next());
        assertEquals("child.grandChild", grandChild.path());
    }
}