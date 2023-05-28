package one.xingyi.optics.annotations.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.optics.annotations.Optics;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Arrays.asList;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public final class RecordOpticsDetails {
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


    static RecordOpticsDetails fromFieldDetails(String packageName, String className, boolean addListTraversal, List<FieldDetails> fieldDetails, List<TraversalDetails> traversalDetails, boolean debug) {
        return new RecordOpticsDetails(packageName, className, addListTraversal, fieldDetails.stream().map(fd -> ViewFieldDetails.oneForRecord(className, fieldDetails, fd)).toList(), traversalDetails, debug);
    }

    static Function<Element, RecordOpticsDetails> fromElement(Consumer<String> log) {
        return element -> {
            Optics annotation = element.getAnnotation(Optics.class);
            boolean debug = annotation.debug();
            if (debug) log.accept("Optics - Found " + element + " with annotation " + annotation);
            List<TraversalDetails> traversals = asList(annotation.traversals()).stream().map(TraversalDetails::fromPath).toList();
            var pckElement = element.getEnclosingElement();
            if (!pckElement.getKind().toString().equals("PACKAGE"))
                throw new RuntimeException("Expected package for " + element + " but got " + pckElement.getKind());
            var pckName = pckElement.toString();
            var className = element.getSimpleName().toString();
            List<FieldDetails> fieldDetails = element.getEnclosedElements().stream().filter(e -> e.getKind().toString().equals("RECORD_COMPONENT")).map(e -> {
                var name = e.getSimpleName().toString();
                var type = e.asType().toString();
                int index = type.indexOf("<");
                var containedFieldType = index >= 0 ? type.substring(index + 1, type.lastIndexOf(">")) : null;
                var simpleCollectionType = index >= 0 ? Utils.lastSegment(type.substring(0, index)) : null;
                return new FieldDetails(PackageAndClass.from(type), PackageAndClass.from(simpleCollectionType), PackageAndClass.from(containedFieldType), name);
            }).toList();
            return fromFieldDetails(pckName, className, annotation.addListTraversal(), fieldDetails, traversals, debug);
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


    public static ViewFieldDetails oneForRecord(String className, List<FieldDetails> fds, FieldDetails fd) {
        var setterParts = fds.stream().map(f -> f == fd ? "value" : "main." + f.getName() + "()").toList();
        var setter = "(main,value)->new " + className + "(" + String.join(",", setterParts) + ")";


        return new ViewFieldDetails(fd.getFieldType(), fd.getSimpleCollectionType(), fd.getContainedFieldType(), fd.getName(), setter);
    }
}

record TraversalDetails(String name, List<String> path) {
    public static TraversalDetails fromPath(String path) {
        var defName = path.replace('.', '_') + "T";
        var name = Utils.firstPart(path, ":", defName);
        var actualPath = Utils.lastPart(path, ":", path);
        return new TraversalDetails(name, asList(actualPath.split("\\.")));
    }
}

