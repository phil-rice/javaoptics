package one.xingyi.utils;

import java.util.Optional;
import java.util.stream.Stream;

public interface StreamHelper {
    static <T> Stream<T> streamOf(Optional<T> opt) {
        return opt.map(Stream::of).orElseGet(Stream::empty);
    }
}
