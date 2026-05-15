package one.xingyi.helpers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface StreamHelper {
    static <T> Stream<T> streamOf(Optional<T> opt) {
        return opt.map(Stream::of).orElseGet(Stream::empty);
    }
    static <T> Stream<T> lastN(Stream<T> s, int n) {
        List<T> list = s.collect(Collectors.toList());
        return list.subList(Math.max(0, list.size() - n), list.size()).stream();
    }
    static <T> T last(Stream<T> s) {
        return s.reduce(null, (acc, v) -> v);
    }

    static <T> T first(Stream<T> s) {
        return s.findFirst().orElse(null);
    }

}
