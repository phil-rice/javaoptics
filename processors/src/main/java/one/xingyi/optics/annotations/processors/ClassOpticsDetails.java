package one.xingyi.optics.annotations.processors;

import lombok.*;
import one.xingyi.optics.annotations.Optics;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public final class ClassOpticsDetails {
    private final String packageName;
    private final String className;
    private final boolean addListTraversal;
    private final List<ViewFieldDetails> fieldDetails;
    private final List<TraversalDetails> traversalDetails;
    private final boolean debug;


    public String getCanonicalName() {
        return packageName + "." + className;
    }

    public PackageAndClass getPackageAndClass() {
        return new PackageAndClass(null, packageName, className);
    }

    Optional<ViewFieldDetails> find(String name) {
        return fieldDetails.stream().filter(vfd -> vfd.name.equals(name)).findFirst();

    }


    static ClassOpticsDetails fromFieldDetails(String packageName, String className, boolean addListTraversal, List<FieldDetails> fieldDetails, List<TraversalDetails> traversalDetails, boolean debug) {
        return new ClassOpticsDetails(packageName, className, addListTraversal, fieldDetails.stream()
                .map(fd -> ViewFieldDetails.oneForRecord(className, fieldDetails, fd)).collect(Collectors.toList()), traversalDetails, debug);
    }

    static ElementKind selectFieldType(Consumer<String> log, Element element) {
        if (element.getKind().toString().equals("RECORD")) return ElementKind.valueOf("RECORD_COMPONENT");
        if (element.getKind().equals(CLASS)) return FIELD;
//        log.accept("Optics " + element + " is not a record or a class. Kind is " + element.getKind());
        throw new RuntimeException("Optics " + element + " is not a record or a class. Kind is " + element.getKind());
    }

    static Function<String, String> getterFn(Element element) {
        if (element.getKind().toString().equals("RECORD")) return Function.identity();
        if (element.getKind().equals(CLASS)) return s -> "get" + s.substring(0, 1).toUpperCase() + s.substring(1);
//        log.accept("Optics " + element + " is not a record or a class. Kind is " + element.getKind());
        throw new RuntimeException("Optics " + element + " is not a record or a class. Kind is " + element.getKind());
    }

    protected static Function<Element, ClassOpticsDetails> makeDetailFromElement(Consumer<String> log) {
        return element -> {
            Optics annotation = element.getAnnotation(Optics.class);
            boolean debug = annotation.debug();
            if (debug)
                log.accept("Optics - Found " + element + " of kind" + element.getKind() + " with annotation " + annotation);

            ElementKind fieldKind = selectFieldType(log, element);
            List<TraversalDetails> traversals = Arrays.stream(annotation.traversals()).map(TraversalDetails::fromPath).collect(Collectors.toList());
            var pckElement = element.getEnclosingElement();
            if (!pckElement.getKind().toString().equals("PACKAGE"))
                throw new RuntimeException("Expected package for " + element + " but got " + pckElement.getKind());
            var pckName = pckElement.toString();
            var className = element.getSimpleName().toString();
            log.accept("fieldKind" + fieldKind + " And enclosed " + element.getEnclosedElements().stream().map(e -> e.getKind() + " " + e).collect(Collectors.joining(",")));
            List<FieldDetails> fieldDetails = element.getEnclosedElements().stream().filter(e -> e.getKind().equals(fieldKind))
                    .map(ClassOpticsDetails.makeFieldDetailsFromElement(getterFn(element))).collect(Collectors.toList());
            return fromFieldDetails(pckName, className, annotation.addListTraversal(), fieldDetails, traversals, debug);
        };
    }

    private static Function<Element, FieldDetails> makeFieldDetailsFromElement(Function<String, String> getterFn) {
        return e -> {
            var name = e.getSimpleName().toString();
            var type = e.asType().toString();
            int index = type.indexOf("<");
            var containedFieldType = index >= 0 ? type.substring(index + 1, type.lastIndexOf(">")) : null;
            var simpleCollectionType = index >= 0 ? Utils.lastSegment(type.substring(0, index)) : null;
            String getter = getterFn.apply(name);
            return new FieldDetails(PackageAndClass.from(type), PackageAndClass.from(simpleCollectionType),
                    PackageAndClass.from(containedFieldType), name, getter);
        };
    }

    private static String getContainedFieldType(String type) {
        return type.contains("<") ? type.substring(type.indexOf("<") + 1, type.lastIndexOf(">")) : null;
    }


}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class FieldDetails {
    protected final PackageAndClass fieldType;
    protected final PackageAndClass simpleCollectionType;
    protected final PackageAndClass containedFieldType;
    protected final String name;
    protected final String getter;
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class ViewFieldDetails {
    protected final PackageAndClass fieldType;
    protected final PackageAndClass simpleCollectionType;
    protected final PackageAndClass containedFieldType;
    protected final String name;
    protected final String setter;
    protected final String getter;


    public static ViewFieldDetails oneForRecord(String className, List<FieldDetails> fds, FieldDetails fd) {
        var setterParts = fds.stream().map(f -> f == fd ? "value" : "main." + f.getter + "()").collect(Collectors.toList());
        var setter = "(main,value)->new " + className + "(" + String.join(",", setterParts) + ")";
        return new ViewFieldDetails(fd.getFieldType(), fd.getSimpleCollectionType(), fd.getContainedFieldType(), fd.getName(), setter, fd.getter);
    }
}

@RequiredArgsConstructor
@ToString
class TraversalDetails {
    public final String name;
    public final List<String> path;
    public static TraversalDetails fromPath(String path) {
        var defName = path.replace('.', '_') + "T";
        var name = Utils.firstPart(path, ":", defName);
        var actualPath = Utils.lastPart(path, ":", path);
        return new TraversalDetails(name, asList(actualPath.split("\\.")));
    }
}

