package one.xingyi.annotations.processors;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record RecordedTraversals(PackageAndClass className, boolean debug, List<NameAndType> classAndFields) {
    static BiFunction<WithDebug<PackageAndClass>, String, RecordedTraversals> parse = (from, s) -> {
        var lines = s.split("\n");
        var namesAndTypes = Arrays.stream(lines).map(String::trim).filter(s1 -> !s1.isEmpty()).
                flatMap(line -> {
                    String name = Utils.firstPart(line, ":", null);
                    String packageAndClass = Utils.lastPart(line, ":", null);
                    if (name == null || packageAndClass == null) return Stream.empty();
                    return Stream.of(new NameAndType(name, PackageAndClass.from(packageAndClass)));
                }).toList();
        return new RecordedTraversals(from.t(), from.debug(), namesAndTypes);

    };

    static Function<RecordedTraversals, String> printer = s -> {
        var classAndFields = s.classAndFields.stream().map(NameAndType::toString).collect(Collectors.joining("\n"));
        return s.className.getString() + ";" + s.debug + ";" + classAndFields;
    };
}


record NameAndType(String fieldName, PackageAndClass fieldType) {

}
