package one.xingyi.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public class PackageAndClass {
    private final String packageName;
    private final String className;

    public static Map<String, PackageAndClass> primitives =
            Map.of("int", new PackageAndClass("java.lang", "Integer"),
                    "long", new PackageAndClass("java.lang", "Long"),
                    "double", new PackageAndClass("java.lang", "Double"),
                    "boolean", new PackageAndClass("java.lang", "Boolean"),
                    "float", new PackageAndClass("java.lang", "Float"),
                    "char", new PackageAndClass("java.lang", "Character"),
                    "byte", new PackageAndClass("java.lang", "Byte"),
                    "short", new PackageAndClass("java.lang", "Short"),
                    "void", new PackageAndClass("java.lang", "Void"));

    public static PackageAndClass from(String fullName) {
        if (fullName == null) return null;
        var result = primitives.get(fullName);
        if (result != null) return result;
        int lastDot = fullName.lastIndexOf('.');
        if (lastDot == -1) return new PackageAndClass("", fullName);
        return new PackageAndClass(fullName.substring(0, lastDot), fullName.substring(lastDot + 1));
    }

    public String getString() {
        return packageName + "." + className;
    }
}
