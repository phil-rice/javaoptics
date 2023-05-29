package one.xingyi.optics;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ILens<Main, Child> extends IOptional<Main, Child> {

    static <Main, Child> ILens<Main, Child> of(Function<Main, Child> getFn, BiFunction<Main, Child, Main> setFn) {
        return new Lens<>(getFn, setFn);
    }

    Child get(Main main);

    Main set(Main main, Child child);

    <GrandChild> ILens<Main, GrandChild> chainLens(ILens<Child, GrandChild> t);
}

abstract class AbstractLens<Main, Child> extends AbstractOptional<Main, Child> implements ILens<Main, Child> {
    @Override
    public Stream<Child> all(Main main) {
        return Stream.of(get(main));
    }

    @Override
    public Main modify(Main main, Function<Child, Child> fn) {
        return set(main, fn.apply(get(main)));
    }

    @Override
    public Optional<Child> optGet(Main main) {
        return Optional.of(get(main));
    }

    @Override
    public Main optSet(Main main, Child child) {
        return set(main, child);
    }

    @Override
    public <GrandChild> ILens<Main, GrandChild> chainLens(ILens<Child, GrandChild> t) {
        return new Lens<>(
                main -> t.get(get(main)),
                (main, grandChild) -> set(main, t.set(get(main), grandChild))
        );
    }
}

final class Lens<Main, Child> extends AbstractLens<Main, Child> implements ILens<Main, Child> {
    private final Function<Main, Child> get;
    private final BiFunction<Main, Child, Main> set;

    Lens(Function<Main, Child> get,
         BiFunction<Main, Child, Main> set) {
        this.get = get;
        this.set = set;
    }

    @Override
    public Child get(Main main) {
        return get.apply(main);
    }

    @Override
    public Main set(Main main, Child child) {
        return set.apply(main, child);
    }

    @Override
    public Main modify(Main main, Function<Child, Child> fn) {
        return set.apply(main, fn.apply(get.apply(main)));
    }
}
