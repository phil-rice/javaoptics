package one.focuson.optics;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ITraversal<Main, Child> extends IFold<Main, Child> {
    static <Main, Child> ITraversal<Main, Child> of(Function<Main, Stream<Child>> allFn,
                                                    BiFunction<Main, Function<Child, Child>, Main> modifyFn) {
        return new Traversal<>(allFn, modifyFn);
    }

    Main modify(Main main, Function<Child, Child> fn);

    <GrandChild> ITraversal<Main, GrandChild> chainTraversal(ITraversal<Child, GrandChild> t);

    <GrandChild> ITraversal<Main, GrandChild> chain(IOptional<Child, GrandChild> t);

    ITraversal<Main, Child> filter(Predicate<Child> p);

}

abstract class AbstractTraversal<Main, Child> extends AbstractFold<Main, Child> implements ITraversal<Main, Child> {
    public <GrandChild> Traversal<Main, GrandChild> chainTraversal(ITraversal<Child, GrandChild> t) {
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
