package one.xingyi.optics;

import one.xingyi.tuples.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IFold<Main, Child> {
    static <Main, Child> IFold<Main, Child> of(Function<Main, Stream<Child>> allFn) {
        return new Fold<>(allFn);
    }

    Stream<Child> all(Main main);

    <GrandChild> IFold<Main, GrandChild> chainFold(IFold<Child, GrandChild> t);

    IFold<Main, Child> filter(Predicate<Child> p);

    <Child2, Merged> IFold<Main, Merged> merge(IFold<Main, Child2> other, IISO<Tuple2<Child, Child2>, Merged> iso);

}

abstract class AbstractFold<Main, Child> implements IFold<Main, Child> {
    public <GrandChild> IFold<Main, GrandChild> chainFold(IFold<Child, GrandChild> f2) {
        return new Fold<Main, GrandChild>(main -> this.all(main).flatMap(f2::all));
    }

    public IFold<Main, Child> filter(Predicate<Child> p) {
        return new Fold<>(main -> this.all(main).filter(p));
    }

    public <Child2, Merged> IFold<Main, Merged> merge(IFold<Main, Child2> other, IISO<Tuple2<Child, Child2>, Merged> iso) {
        return new Fold<>(main -> this.all(main).flatMap(child -> other.all(main).map(child2 -> iso.get(Tuple2.of(child, child2)))));
    }
}

class Fold<Main, Child> extends AbstractFold<Main, Child> implements IFold<Main, Child> {

    protected final Function<Main, Stream<Child>> allFn;

    Fold(Function<Main, Stream<Child>> allFn) {
        this.allFn = allFn;
    }


    public Stream<Child> all(Main main) {
        return allFn.apply(main);
    }
}