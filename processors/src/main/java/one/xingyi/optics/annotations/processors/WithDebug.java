package one.xingyi.optics.annotations.processors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class WithDebug<T> {
    public final T t;
    public final boolean debug;
    public static <T> WithDebug<T> of(T t, boolean debug) {
        return new WithDebug<>(t, debug);
    }
}
