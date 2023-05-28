package one.xingyi.annotations.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.function.Consumer;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public class RecordOpticsWithTraversals {
    private final String packageName;
    private final String className;
    private final boolean addListTraversal;
    private final List<ViewFieldDetails> fieldDetails;
    private final List<TraversalWithFullDetails> traversalDetails;
    private final boolean debug;

    public static RecordOpticsWithTraversals from(List<RecordOpticsDetails> allDetails, RecordOpticsDetails details, Consumer<String> log) {
        return new RecordOpticsWithTraversals(details.getPackageName(), details.getClassName(), details.isAddListTraversal(), details.getFieldDetails(),
                details.getTraversalDetails().stream().map(t -> TraversalWithFullDetails.from(allDetails, details, t, log)).toList(), details.isDebug());
    }

}

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
class TraversalWithFullDetails {
    protected final String name;
    protected final List<RecordAndField> path;

    static TraversalWithFullDetails from(List<RecordOpticsDetails> allDetails, RecordOpticsDetails recordOpticsDetails, TraversalDetails details, Consumer<String> log) {
        var recordsAndFields = RecordAndField.fromPath(allDetails, recordOpticsDetails, details.path(), 0, log);
        return new TraversalWithFullDetails(details.name(), recordsAndFields);
    }

    public PackageAndClass getClassAtEnd() {
        return path.get(path.size() - 1).field.getContainedFieldType();
    }

    @Override
    public String toString() {
        return "TraversalWithFullDetails(" + this.name + ", " + this.path.stream()
                .map(p -> p.record.getClassName() + '.' + p.field.name).toList() + "classAtEnd=" + getClassAtEnd() + ")";
    }
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class RecordAndField {
    final RecordOpticsDetails record;
    final ViewFieldDetails field;
    final String pathPart;

    static List<RecordAndField> fromPath(List<RecordOpticsDetails> allDetails, RecordOpticsDetails details, List<String> path, int index, Consumer<String> log) {
        if (index >= path.size()) return List.of();
        var foundField = details.find(path.get(index));
        if (details.isDebug())
            log.accept("fromPath: " + details.getClassName() + " " + path + " " + index + " " + foundField);
        if (foundField.isEmpty()) return List.of();
        var fieldDetails = foundField.get();
        RecordAndField recordAndField = new RecordAndField(details, fieldDetails, path.get(index));
        if (index == path.size() - 1) return List.of(recordAndField);

        if (details.isDebug()) {
            log.accept("   allDetails: " + allDetails.stream().map(RecordOpticsDetails::getCanonicalName));
            log.accept("   looking for " + fieldDetails.containedFieldType.getString());
        }
        var newDetails = allDetails.stream().filter(d -> d.getCanonicalName().equals(fieldDetails.containedFieldType.getString())).findFirst();
        if (newDetails.isEmpty()) return List.of(recordAndField);
        var restOfList = fromPath(allDetails, newDetails.get(), path, index + 1, log);
        return Utils.insert(restOfList, recordAndField);
    }

}


