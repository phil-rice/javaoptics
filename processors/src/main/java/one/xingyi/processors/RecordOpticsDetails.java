package one.xingyi.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.lang.model.element.Element;
import java.util.List;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public final class RecordOpticsDetails {
    private final String packageName;
    private final String className;
    private final List<ViewFieldDetails> fieldDetails;

    static RecordOpticsDetails fromFieldDetails(String packageName, String className, List<FieldDetails> fieldDetails) {
        return new RecordOpticsDetails(packageName, className, fieldDetails.stream().map(fd -> ViewFieldDetails.oneForRecord(className, fieldDetails, fd)).toList());
    }

    static RecordOpticsDetails fromElement(Element element) {
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
            return new FieldDetails(type, simpleCollectionType, containedFieldType, name);
        }).toList();
        return fromFieldDetails(pckName, className, fieldDetails);
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
    private final String fieldType;
    private final String simpleCollectionType;
    private final String containedFieldType;
    private final String name;
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class ViewFieldDetails {
    private final String fieldType;
    private final String simpleCollectionType;
    private final String containedFieldType;
    private final String name;
    private final String setter;


    public static ViewFieldDetails oneForRecord(String className, List<FieldDetails> fds, FieldDetails fd) {
        var setterParts = fds.stream().map(f -> f == fd ? "value" : "main." + f.getName() + "()").toList();
        var setter = "(main,value)->new " + className + "(" + String.join(",", setterParts) + ")";


        return new ViewFieldDetails(fd.getFieldType(), fd.getSimpleCollectionType(), fd.getContainedFieldType(), fd.getName(), setter);
    }

}
