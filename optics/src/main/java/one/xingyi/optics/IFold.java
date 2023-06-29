package one.xingyi.optics;

import lombok.var;
import one.xingyi.fp.StreamComprehensionsForExceptions;
import one.xingyi.interfaces.ConsumerWithException;
import one.xingyi.interfaces.FunctionWithException;
import one.xingyi.tuples.Tuple2;
import one.xingyi.utils.StreamHelper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IFold<Main, Child> {
    static <Main, Child> IFold<Main, Child> of(Function<Main, Stream<Child>> allFn) {
        return new Fold<>(allFn);
    }

    static <Main, Child> IFold<Main, Child> ofWithoutNulls(Function<Main, Stream<Child>> allFn) {
        return new Fold<>(main -> allFn.apply(main).filter(Objects::nonNull));
    }

    static <T> IFold<Collection<T>, T> collectionFold() {
        return new Fold<>(Collection::stream);
    }
    Stream<Child> all(Main main);
    List<Child> allAsList(Main main);

    <GrandChild> IFold<Main, GrandChild> chainFold(IFold<Child, GrandChild> t);

    void forEach(Main main, ConsumerWithException<Child> consumer) throws Exception;
    IFold<Main, Child> lastN(int n);

    IFold<Main, Child> filter(Predicate<Child> p);

    IFold<Main, Child> unique();
    <T> IFold<Main, T> map(Function<Child, T> fn);
    <Child2, Merged> IFold<Main, Merged> merge(IFold<Main, Child2> other, IISO<Tuple2<Child, Child2>, Merged> iso);

}

abstract class AbstractFold<Main, Child> implements IFold<Main, Child> {
    public <GrandChild> IFold<Main, GrandChild> chainFold(IFold<Child, GrandChild> f2) {
        return new Fold<Main, GrandChild>(main -> this.all(main).flatMap(f2::all));
    }

    @Override
    public List<Child> allAsList(Main main) {
        return all(main).collect(Collectors.toList());
    }
    public IFold<Main, Child> lastN(int n) {
        return IFold.of(main -> StreamHelper.lastN(all(main), n));
    }

    public IFold<Main, Child> filter(Predicate<Child> p) {
        return new Fold<>(main -> this.all(main).filter(p));
    }
    @Override
    public <T> IFold<Main, T> map(Function<Child, T> fn) {
        return new Fold<Main, T>(main -> this.all(main).map(fn));
    }
    public <Child2, Merged> IFold<Main, Merged> merge(IFold<Main, Child2> other, IISO<Tuple2<Child, Child2>, Merged> iso) {
        return new Fold<>(main -> this.all(main).flatMap(child -> other.all(main).map(child2 -> iso.get(Tuple2.of(child, child2)))));
    }
    @Override
    public void forEach(Main main, ConsumerWithException<Child> consumer) throws Exception {
        for (Iterator<Child> iterator = all(main).iterator(); iterator.hasNext(); )
            consumer.accept(iterator.next());
    }
    @Override
    public IFold<Main, Child> unique() {
        return IFold.of(main -> all(main).distinct());
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