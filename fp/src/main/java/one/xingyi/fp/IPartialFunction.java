package one.xingyi.fp;

import lombok.RequiredArgsConstructor;
import lombok.var;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IPartialFunction<From, To> extends Function<From, To> {
    boolean isDefinedAt(From from);




    static <From, To> IPartialFunction<From, To> always(Function<From, To> fn) {
        return IPartialFunction.of(q -> true, fn);
    }
    static <From, To> To applyOrError(IPartialFunction<From, To> pfn, From from) {
        if (pfn.isDefinedAt(from))
            return pfn.apply(from);
        throw new IllegalArgumentException("Not defined at " + from);
    }
    static <From, To> To applyOr(IPartialFunction<From, To> pfn, Supplier<To> defValue, From from) {
        return pfn.isDefinedAt(from) ? pfn.apply(from) : defValue.get();
    }
    static <From, To> IPartialFunction<From, To> of(Predicate<From> isDefinedAt, Function<From, To> fn) {
        return new PartialFunction<>(isDefinedAt, fn);
    }
    static <From, To> IPartialFunction<From, To> notNull(Function<From, To> fn) {
        return IPartialFunction.of(Objects::nonNull, fn);
    }
    static <From, To> Function<From, List<To>> mapFn(Collection<IPartialFunction<From, To>> fns) {
        return from -> {
            List<To> result = new ArrayList<>();
            for (IPartialFunction<From, To> fn : fns)
                if (fn.isDefinedAt(from))
                    result.add(fn.apply(from));
            return result;
        };
    }
    static <From, To, Result> Function<From, Result> mapReduceFn(Collection<IPartialFunction<From, To>> fns, Function<List<To>, Result> reduceFn) {
        var fn = mapFn(fns);
        return from -> reduceFn.apply(fn.apply(from));
    }
    static <From, To> Function<From, To> chain(To defValue, Collection<IPartialFunction<From, To>> fns) {
        return from -> {
            for (IPartialFunction<From, To> fn : fns)
                if (fn.isDefinedAt(from))
                    return fn.apply(from);
            return defValue;
        };
    }
    static <From, To> IPartialFunction<From, To> chainToPf(Collection<IPartialFunction<From, To>> pfs) {
        return IPartialFunction.of(
                from -> pfs.stream().anyMatch(pf -> pf.isDefinedAt(from)),
                from -> {
                    for (IPartialFunction<From, To> pf : pfs)
                        if (pf.isDefinedAt(from))
                            return pf.apply(from);
                    throw new IllegalArgumentException("Software error: not defined at " + from);
                });

    }
}
@RequiredArgsConstructor
class PartialFunction<From, To> implements IPartialFunction<From, To> {
    final Predicate<From> isDefinedAt;
    final Function<From, To> fn;
    @Override
    public boolean isDefinedAt(From from) {
        return isDefinedAt.test(from);
    }
    @Override
    public To apply(From from) {
        return fn.apply(from);
    }
}
