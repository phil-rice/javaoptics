package one.focuson.optics;

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


}

abstract class AbstractFold<Main, Child> implements IFold<Main, Child> {
    public <GrandChild> IFold<Main, GrandChild> chainFold(IFold<Child, GrandChild> f2) {
        return new Fold<Main, GrandChild>(main -> this.all(main).flatMap(f2::all));
    }

    public IFold<Main, Child> filter(Predicate<Child> p) {
        return new Fold<>(main -> this.all(main).filter(p));
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