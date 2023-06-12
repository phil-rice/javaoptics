package one.xingyi.optics.annotations.processors;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public class PackageAndClass {
    private final PackageAndClass holdingClass;
    private final String packageName;
    private final String className;
    public PackageAndClass mapClassName(Function<String, String> fn) {
        return new PackageAndClass(holdingClass, packageName, fn.apply(className));
    }


    public static Map<String, PackageAndClass> primitives = makePrimitives();

    private static Map<String, PackageAndClass> makePrimitives() {
        Map<String, PackageAndClass> result = new HashMap<>();
        result.put("int", new PackageAndClass(null, "java.lang", "Integer"));
        result.put("long", new PackageAndClass(null, "java.lang", "Long"));
        result.put("double", new PackageAndClass(null, "java.lang", "Double"));
        result.put("boolean", new PackageAndClass(null, "java.lang", "Boolean"));
        result.put("float", new PackageAndClass(null, "java.lang", "Float"));
        result.put("char", new PackageAndClass(null, "java.lang", "Character"));
        result.put("byte", new PackageAndClass(null, "java.lang", "Byte"));
        result.put("short", new PackageAndClass(null, "java.lang", "Short"));
        result.put("void", new PackageAndClass(null, "java.lang", "Void"));
        return result;
    }


    public static PackageAndClass from(String fullName) {
        if (fullName == null) return null;
        int index = fullName.indexOf("<");
        if (index > 0) return fromHolding(fullName, index);

        var result = primitives.get(fullName);
        if (result != null) return result;
        int lastDot = fullName.lastIndexOf('.');
        if (lastDot == -1) return new PackageAndClass(null, "", fullName);
        return new PackageAndClass(null, fullName.substring(0, lastDot), fullName.substring(lastDot + 1));
    }

    public static PackageAndClass fromHolding(String fullName, int indexOfFirst) {
        var first = fullName.substring(0, indexOfFirst);
        var last = fullName.substring(indexOfFirst + 1).replace(">", "");
        var holding = from(first);
        var held = from(last);
        return new PackageAndClass(holding, held.packageName, held.className);

    }


    public String getShortName() {
        return holdingClass != null ? holdingClass.getShortName() + "<" + className + ">" : className;
    }
    public String getString() {
        return packageName + "." + className;
    }
}
