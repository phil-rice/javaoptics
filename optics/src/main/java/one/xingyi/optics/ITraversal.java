package one.xingyi.optics;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ITraversal<Main, Child> extends IFold<Main, Child> {
    static <Main, Child> ITraversal<Main, Child> of(Function<Main, Stream<Child>> allFn,
                                                    BiFunction<Main, Function<Child, Child>, Main> modifyFn) {
        return new Traversal<>(allFn, modifyFn);
    }

    static <T> ITraversal<List<T>, T> listTraversal() {
        return new Traversal<>(List::stream, (list, fn) -> list.stream().map(fn).toList());
    }

    static <Main, Child> ITraversal<Main, Child> fromListLens(ILens<Main, List<Child>> lens) {
        return new Traversal<>(main -> lens.get(main).stream(), (main, fn) -> lens.set(main, lens.get(main).stream().map(fn).toList()));
    }

    static <Main, Child> ITraversal<Main, Child> fromCollectionLens(ILens<Main, Collection<Child>> lens) {
        return new Traversal<>(main -> lens.get(main).stream(), (main, fn) -> lens.set(main, lens.get(main).stream().map(fn).toList()));
    }

    static <Main, Child> ITraversal<Main, Child> fromStreamLens(ILens<Main, Stream<Child>> lens) {
        return new Traversal<>(lens::get, (main, fn) -> lens.set(main, lens.get(main).map(fn)));
    }

    Main modify(Main main, Function<Child, Child> fn);

    <GrandChild> ITraversal<Main, GrandChild> andThen(ITraversal<Child, GrandChild> t);

    <GrandChild> ITraversal<Main, GrandChild> chain(IOptional<Child, GrandChild> t);

    ITraversal<Main, Child> filter(Predicate<Child> p);

    void forEach(Main main, Consumer<Child> fn);

}

abstract class AbstractTraversal<Main, Child> extends AbstractFold<Main, Child> implements ITraversal<Main, Child> {
    public <GrandChild> Traversal<Main, GrandChild> andThen(ITraversal<Child, GrandChild> t) {
        return new Traversal<>(
                main -> all(main).flatMap(t::all),
                (main, fn) -> modify(main, child -> t.modify(child, fn))
        );
    }

    public Traversal<Main, Child> filter(Predicate<Child> p) {
        return new Traversal<>(main -> all(main).filter(p),
                (main, fn) -> modify(main, child -> p.test(child) ? fn.apply(child) : child));
    }

    @Override
    public <GrandChild> ITraversal<Main, GrandChild> chain(IOptional<Child, GrandChild> t) {
        return new Traversal<>(
                main -> all(main).flatMap(child -> t.optGet(child).stream()),
                (main, fn) -> modify(main, child -> t.optGet(child).map(fn).map(grandChild -> t.optSet(child, grandChild)).orElse(child))
        );
    }

    @Override
    public void forEach(Main main, Consumer<Child> fn) {
        all(main).forEach(fn);
    }
}

class Traversal<Main, Child> extends AbstractTraversal<Main, Child> implements ITraversal<Main, Child> {

    protected final Function<Main, Stream<Child>> allFn;
    protected final BiFunction<Main, Function<Child, Child>, Main> modifyFn;

    Traversal(Function<Main, Stream<Child>> allFn, BiFunction<Main, Function<Child, Child>, Main> modifyFn) {
        this.allFn = allFn;
        this.modifyFn = modifyFn;
    }


    public Main modify(Main main, Function<Child, Child> fn) {
        return modifyFn.apply(main, fn);
    }

    @Override
    public Stream<Child> all(Main main) {
        return allFn.apply(main);
    }
}
