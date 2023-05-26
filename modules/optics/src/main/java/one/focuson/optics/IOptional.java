package one.focuson.optics;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface IOptional<Main, Child> extends ITraversal<Main, Child> {
    static <Main, Child> IOptional<Main, Child> of(Function<Main, Optional<Child>> optGetFn, BiFunction<Main, Child, Main> optSetFn) {
        return new OptionalOptic<>(optGetFn, optSetFn);
    }

    Optional<Child> optGet(Main main);

    Main optSet(Main main, Child child);

    <GrandChild> IOptional<Main, GrandChild> chainOptional(IOptional<Child, GrandChild> t);
}

abstract class AbstractOptional<Main, Child> extends AbstractTraversal<Main, Child> implements IOptional<Main, Child> {
    @Override
    public Stream<Child> all(Main main) {
        Optional<Child> opt = optGet(main);
        return opt.stream();
    }

    @Override
    public Main modify(Main main, Function<Child, Child> fn) {
        Optional<Child> opt = optGet(main);
        return opt.map(fn).map(child -> optSet(main, child)).orElse(main);
    }

    @Override
    public <GrandChild> IOptional<Main, GrandChild> chainOptional(IOptional<Child, GrandChild> t) {
        return new OptionalOptic<>(
                main -> optGet(main).flatMap(t::optGet),
                (main, grandChild) -> optGet(main).map(child -> t.optSet(child, grandChild)).map(child -> optSet(main, child)).orElse(main)
        );
    }
}

class OptionalOptic<Main, Child> extends AbstractOptional<Main, Child> implements IOptional<Main, Child> {
    protected final Function<Main, Optional<Child>> optGetFn;
    protected final BiFunction<Main, Child, Main> optSetFn;

    OptionalOptic(Function<Main, Optional<Child>> optGetFn, BiFunction<Main, Child, Main> optSetFn) {
        this.optGetFn = optGetFn;
        this.optSetFn = optSetFn;
    }

    @Override
    public Optional<Child> optGet(Main main) {
        return Optional.empty();
    }

    @Override
    public Main optSet(Main main, Child child) {
        return optSetFn.apply(main, child);
    }
}
