package one.xingyi.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public class RecordOpticsWithTraversals {
    private final String packageName;
    private final String className;
    private final List<ViewFieldDetails> fieldDetails;
    private final List<TraversalWithFullDetails> traversalDetails;

    public static RecordOpticsWithTraversals from(List<RecordOpticsDetails> allDetails, RecordOpticsDetails details) {
        return new RecordOpticsWithTraversals(details.getPackageName(), details.getClassName(), details.getFieldDetails(),
                details.getTraversalDetails().stream().map(t -> TraversalWithFullDetails.from(allDetails, details, t)).toList());
    }

}

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
class TraversalWithFullDetails {
    protected final String name;
    protected final List<RecordAndField> path;

    static TraversalWithFullDetails from(List<RecordOpticsDetails> allDetails, RecordOpticsDetails recordOpticsDetails, TraversalDetails details) {
        var recordsAndFields = RecordAndField.fromPath(allDetails, recordOpticsDetails, details.path(), 0);
        return new TraversalWithFullDetails(details.name(), recordsAndFields);
    }

    public String getClassAtEnd(){
        return path.get(path.size()-1).field.getContainedFieldType();
    }

    @Override
    public String toString() {
        return "TraversalWithFullDetails(" + this.name + ", " + this.path.stream()
                .map(p -> p.record.getClassName() + '.' + p.field.name).toList() +"classAtEnd="+ getClassAtEnd() + ")";
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

    static List<RecordAndField> fromPath(List<RecordOpticsDetails> allDetails, RecordOpticsDetails details, List<String> path, int index) {
        if (index >= path.size()) return List.of();
        var foundField = details.find(path.get(index));
        if (foundField.isEmpty()) return List.of();
        var fieldDetails = foundField.get();
        RecordAndField recordAndField = new RecordAndField(details, fieldDetails, path.get(index));
        if (index == path.size() - 1) return List.of(recordAndField);

        var newDetails = allDetails.stream().filter(d -> d.getCanonicalName().equals(fieldDetails.containedFieldType)).findFirst();
        if (newDetails.isEmpty()) return List.of(recordAndField);
        var restOfList = fromPath(allDetails, newDetails.get(), path, index + 1);
        return Utils.insert(restOfList, recordAndField);
    }

}


