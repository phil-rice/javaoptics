package one.xingyi.optics.annotations.processors;

public record WithDebug<T>(T t, boolean debug) {
    public static <T> WithDebug<T> of(T t, boolean debug) {
        return new WithDebug<>(t, debug);
    }
}
