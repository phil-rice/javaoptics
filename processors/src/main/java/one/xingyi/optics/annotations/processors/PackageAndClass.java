package one.xingyi.optics.annotations.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
    public PackageAndClass mapClassName(Function<String,String> fn) {
        return new PackageAndClass(holdingClass, packageName, fn.apply(className));
    }


    public static Map<String, PackageAndClass> primitives =
            Map.of("int", new PackageAndClass(null, "java.lang", "Integer"),
                    "long", new PackageAndClass(null, "java.lang", "Long"),
                    "double", new PackageAndClass(null, "java.lang", "Double"),
                    "boolean", new PackageAndClass(null, "java.lang", "Boolean"),
                    "float", new PackageAndClass(null, "java.lang", "Float"),
                    "char", new PackageAndClass(null, "java.lang", "Character"),
                    "byte", new PackageAndClass(null, "java.lang", "Byte"),
                    "short", new PackageAndClass(null, "java.lang", "Short"),
                    "void", new PackageAndClass(null, "java.lang", "Void"));

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
        return holdingClass != null ? holdingClass.getShortName() + "<" + className +">": className;
    }
    public String getString() {
        return packageName + "." + className;
    }
}
