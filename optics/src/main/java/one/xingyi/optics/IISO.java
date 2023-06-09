package one.xingyi.optics;

import one.xingyi.tuples.Tuple2;

import java.util.function.Function;

public interface IISO<Main, Child> extends ILens<Main, Child> {
    static <Main, Child> IISO<Main, Child> of(Function<Main, Child> get, Function<Child, Main> reverseGet) {
        return new Iso<>(get, reverseGet);
    }

    static <T> IISO<T, T> identity() {
        return new Iso<>(t -> t, t -> t);
    }

    Child get(Main main);

    Main reverseGet(Child child);

    <GrandChild> ILens<Main, GrandChild> chainIso(IISO<Child, GrandChild> t);
}

abstract class AbstractIso<Main, Child> extends AbstractLens<Main, Child> implements IISO<Main, Child> {

    @Override
    public Main set(Main main, Child child) {
        return reverseGet(child);
    }

    @Override
    public <GrandChild> ILens<Main, GrandChild> chainIso(IISO<Child, GrandChild> t) {
        return new Iso<>(
                main -> t.get(get(main)),
                grandChild -> reverseGet(t.reverseGet(grandChild))
        );
    }

    @Override
    public <Merged, Child2> ILens<Main, Merged> merge(ILens<Main, Child2> other, IISO<Tuple2<Child, Child2>, Merged> iso) {
        return new Lens<>(main -> iso.get(Tuple2.of(get(main), other.get(main))),
                (main, merged) -> {
                    Tuple2<Child, Child2> tuple2 = iso.reverseGet(merged);
                    return set(other.set(main, tuple2.t2), tuple2.t1);
                });
    }
}

class Iso<Main, Child> extends AbstractIso<Main, Child> {

    protected final Function<Main, Child> get;
    protected final Function<Child, Main> reverseGet;

    Iso(Function<Main, Child> get, Function<Child, Main> reverseGet) {
        this.get = get;
        this.reverseGet = reverseGet;
    }

    @Override
    public Main reverseGet(Child child) {
        return reverseGet.apply(child);
    }

    @Override
    public Child get(Main main) {
        return get.apply(main);
    }

}