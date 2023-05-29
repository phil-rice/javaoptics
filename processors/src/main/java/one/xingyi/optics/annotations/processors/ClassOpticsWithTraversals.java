package one.xingyi.optics.annotations.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.optics.annotations.serialise.IAnnotationProcessorLoader;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public class ClassOpticsWithTraversals {
    private final String packageName;
    private final String className;
    private final boolean addListTraversal;
    private final List<ViewFieldDetails> fieldDetails;
    private final List<TraversalWithFullDetails> traversalDetails;
    private final boolean debug;

    public static ClassOpticsWithTraversals from(IAnnotationProcessorLoader<WithDebug<PackageAndClass>, RecordedTraversals> store, ClassOpticsDetails details, Consumer<String> log) throws IOException {
        List<TraversalWithFullDetails> tds = Utils.map(details.getTraversalDetails(), td -> TraversalWithFullDetails.from(details, td, store, log));
        return new ClassOpticsWithTraversals(details.getPackageName(), details.getClassName(), details.isAddListTraversal(), details.getFieldDetails(),
                tds, details.isDebug());
    }

}

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
class TraversalWithFullDetails {
    protected final String name;
    protected final List<TraversalPathPart> path;

    public PackageAndClass getClassAtEnd(){
        return path.get(path.size()-1).field.getFieldType();
    }
    static TraversalWithFullDetails from(ClassOpticsDetails recordDetails, TraversalDetails details, IAnnotationProcessorLoader<WithDebug<PackageAndClass>, RecordedTraversals> store, Consumer<String> log) throws IOException {
        var debug = recordDetails.isDebug();
        var path = details.path();
        if (path.size() == 0) throw new RuntimeException("No traversals for " + recordDetails.getClassName());
        String first = path.get(0);
        var rest = path.subList(1, path.size());
        Optional<ViewFieldDetails> firstField = recordDetails.getFieldDetails().stream().filter(fd -> fd.name.equals(first)).findFirst();
        if (firstField.isEmpty())
            throw new RuntimeException("Could not find " + first + " in " + recordDetails.getClassName());
        var firstPart = new TraversalPathPart(recordDetails.getPackageAndClass(), new NameAndType(firstField.get().name, firstField.get().containedFieldType));

        List<TraversalPathPart> tds = Utils.foldLeft(rest, List.of(firstPart), (acc, v) -> {
            var last = acc.get(acc.size() - 1);
            PackageAndClass lastFieldType = last.field.getFieldType();
            RecordedTraversals loaded = store.load(WithDebug.of(lastFieldType, recordDetails.isDebug()));
            if (debug) log.accept("Loaded for path (" + v + ") => " + loaded);
            var found = loaded.classAndFields().stream().filter(nat -> nat.getFieldName().equals(v)).findFirst();
            if (found.isEmpty())
                throw new RuntimeException("Could not find " + v + " in " + lastFieldType + " for " + recordDetails.getClassName() + ":" + path);
            return Utils.append(acc, new TraversalPathPart(lastFieldType, found.get()));
        });
        return new TraversalWithFullDetails(details.name(), tds);
    }


    @Override
    public String toString() {
        return "TraversalWithFullDetails(" + this.name + ", " + this.path.stream().map(TraversalPathPart::toString).collect(Collectors.joining(",")) + ")";
    }
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class TraversalPathPart {
    final PackageAndClass main;
    final NameAndType field;

}


