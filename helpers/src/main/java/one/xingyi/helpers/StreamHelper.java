package one.xingyi.helpers;

import lombok.var;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface StreamHelper {
    static <T> Stream<T> streamOf(Optional<T> opt) {
        return opt.map(Stream::of).orElseGet(Stream::empty);
    }
    static <T> Stream<T> lastN(Stream<T> s, int n) {
        var list = s.collect(Collectors.toList());
        return list.subList(Math.max(0, list.size() - n), list.size()).stream();
    }
    static <T> T last(Stream<T> s) {
        return s.reduce(null, (acc, v) -> v);
    }

    static <T> T first(Stream<T> s) {
        return s.findFirst().orElse(null);
    }

}
