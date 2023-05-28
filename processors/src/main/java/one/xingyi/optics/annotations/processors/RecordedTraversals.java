package one.xingyi.optics.annotations.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record RecordedTraversals(boolean debug, List<NameAndType> classAndFields) {
    static BiFunction<WithDebug<PackageAndClass>, String, RecordedTraversals> parse = (from, s) -> {
        var lines = s.split("\n");
        var namesAndTypes = Arrays.stream(lines).map(String::trim).filter(s1 -> !s1.isEmpty()).
                flatMap(line -> {
                    String name = Utils.firstPart(line, ":", null);
                    String packageAndClass = Utils.lastPart(line, ":", null);
                    if (name == null || packageAndClass == null) return Stream.empty();
                    return Stream.of(new NameAndType(name, PackageAndClass.from(packageAndClass)));
                }).toList();
        return new RecordedTraversals(from.debug(), namesAndTypes);

    };

    static Function<RecordedTraversals, String> printer = s -> {
        return s.classAndFields.stream().map(NameAndType::getString).collect(Collectors.joining("\n"));
    };
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class NameAndType{
    private final String fieldName;
    private final PackageAndClass fieldType;
    String getString() {
        return fieldName + ":" + fieldType.getString();
    }

}
