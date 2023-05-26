package one.focuson.optics;

import java.util.function.Function;

public interface IISO<Main, Child> extends ILens<Main, Child> {
    static <Main, Child> IISO<Main, Child> of(Function<Main, Child> get, Function<Child, Main> reverseGet) {
        return new Iso<>(get, reverseGet);
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