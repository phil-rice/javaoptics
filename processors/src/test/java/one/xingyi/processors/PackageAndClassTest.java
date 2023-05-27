package one.xingyi.processors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PackageAndClassTest {

    @Test
    public void testPackageAndClassForPrimitive() {
        var pc = PackageAndClass.from("int");
        assertEquals("", pc.getClassName());
        assertEquals("java.lang", pc.getPackageName());
        assertEquals("java.lang.Integer", pc.getString());
        assertEquals(null, pc.getHoldingClass());
    }

    @Test
    public void testPackageAndClassForSimple() {
        var pc = PackageAndClass.from("a.b.c.D");
        assertEquals("", pc.getClassName());
        assertEquals("java.lang", pc.getPackageName());
        assertEquals("java.lang.Integer", pc.getString());
        assertEquals(null, pc.getHoldingClass());

    }

    @Test
    public void testPackageAndClassForHigher() {
        var pc = PackageAndClass.from("java.util.List<a.b.c.D>");
        assertEquals("", pc.getClassName());
        assertEquals("java.lang", pc.getPackageName());
        assertEquals("java.lang.Integer", pc.getString());
        assertEquals(null, pc.getHoldingClass());

    }

}