package one.xingyi.optics;

import one.xingyi.tuples.Tuple2;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static one.xingyi.utils.StreamHelper.streamOf;

public interface IOptional<Main, Child> extends ITraversal<Main, Child> {
    static <Main, Child> IOptional<Main, Child> of(Function<Main, Optional<Child>> optGetFn, BiFunction<Main, Child, Optional<Main>> optSetFn) {
        return new OptionalOptic<>(optGetFn, optSetFn);
    }

    Optional<Child> optGet(Main main);

    Optional<Main> optSet(Main main, Child child);

    <GrandChild> IOptional<Main, GrandChild> chainOptional(IOptional<Child, GrandChild> t);

    <Merged, Child2> IOptional<Main, Merged> merge(IOptional<Main, Child2> other, IISO<Tuple2<Child, Child2>, Merged> iso);
}

abstract class AbstractOptional<Main, Child> extends AbstractTraversal<Main, Child> implements IOptional<Main, Child> {
    @Override
    public Stream<Child> all(Main main) {
        Optional<Child> opt = optGet(main);
        return streamOf(opt);
    }

    @Override
    public Main modify(Main main, Function<Child, Child> fn) {
        Optional<Child> opt = optGet(main);
        Optional<Main> result = opt.map(fn).flatMap(child -> optSet(main, child));
        return result.orElse(main);
    }

    @Override
    public <GrandChild> IOptional<Main, GrandChild> chainOptional(IOptional<Child, GrandChild> t) {
        return new OptionalOptic<>(
                main -> optGet(main).flatMap(t::optGet),
                (main, grandChild) -> optGet(main).flatMap(child -> t.optSet(child, grandChild)).flatMap(child -> optSet(main, child))
        );
    }

    public <Merged, Child2> IOptional<Main, Merged> merge(IOptional<Main, Child2> other, IISO<Tuple2<Child, Child2>, Merged> iso) {
        return new OptionalOptic<>(
                main -> optGet(main).flatMap(child -> other.optGet(main).map(child2 -> iso.get(Tuple2.of(child, child2)))),
                (main, merged) -> {
                    Tuple2<Child, Child2> t = iso.reverseGet(merged);
                    return optSet(main, t.t1).flatMap(newMain -> other.optSet(newMain, t.t2));
                }
        );
    }

}

class OptionalOptic<Main, Child> extends AbstractOptional<Main, Child> implements IOptional<Main, Child> {
    protected final Function<Main, Optional<Child>> optGetFn;
    protected final BiFunction<Main, Child, Optional<Main>> optSetFn;

    OptionalOptic(Function<Main, Optional<Child>> optGetFn, BiFunction<Main, Child, Optional<Main>> optSetFn) {
        this.optGetFn = optGetFn;
        this.optSetFn = optSetFn;
    }

    @Override
    public Optional<Child> optGet(Main main) {
        return Optional.empty();
    }

    @Override
    public Optional<Main> optSet(Main main, Child child) {
        return optSetFn.apply(main, child);
    }
}
