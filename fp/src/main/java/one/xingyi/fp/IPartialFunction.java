package one.xingyi.fp;

import lombok.RequiredArgsConstructor;
import lombok.var;
import one.xingyi.helpers.Permutations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface IPartialFunction<From, To> extends Function<From, To> {
    static <From, To> IPartialFunction<From, To> ifThenElse(Predicate<From> condition, Function<From, To> trueFn, Function<From, To> falseFn) {
        return new PartialFunctionIfThenElse<>(condition, trueFn, falseFn);
    }
    boolean isDefinedAt(From from);

    default IPartialFunction<From, To> orElse(IPartialFunction<From, To> other) {
        return IPartialFunction.of(
                from -> isDefinedAt(from) || other.isDefinedAt(from),
                from -> isDefinedAt(from) ? apply(from) : other.apply(from));
    }


    static <From, To> IPartialFunction<From, To> always(Function<From, To> fn) {
        return new PartialFunctionAlwaysTrue<>(q -> true, fn);
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
    static <From, To> IPartialFunction<List<From>, To> listDefined(Function<List<From>, To> fn) {
        return IPartialFunction.of(list -> list != null && list.size() > 0, fn);
    }

    static <From, Child, To> PartialFunction<From, To> fromChild(Function<From, Child> childFn, Predicate<Child> isDefinedAt, Function<Child, To> fn) {
        return new PartialFunction<>(from -> isDefinedAt.test(childFn.apply(from)), from -> fn.apply(childFn.apply(from)));
    }

    static <From, Child, To> PartialFunction<From, To> childNotNull(Function<From, Child> childFn, Function<Child, To> fn) {
        return new PartialFunction<>(from -> childFn.apply(from) != null, from -> fn.apply(childFn.apply(from)));
    }
    static <From, Child, To> PartialFunction<From, To> childListDefined(Function<From, List<Child>> childFn, Function<List<Child>, To> fn) {
        return new PartialFunction<>(from -> {
            List<Child> children = childFn.apply(from);
            return children != null && children.size() > 0;
        }, from -> fn.apply(childFn.apply(from)));
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

    static <From, To> Predicate<List<Boolean>> isOkToUseBooleans(List<IPartialFunction<From, To>> pfns) {
        return booleans -> {
            if (pfns.size() != booleans.size())
                throw new IllegalArgumentException("Software error: pfns and booleans must be the same size. Pfns: " + pfns.size() + " booleans " + booleans.size() + " ->" + booleans);
            for (int i = 0; i < pfns.size(); i++)
                if ((pfns.get(i) instanceof PartialFunctionAlwaysTrue) && !booleans.get(i))
                    return false;
            return true;
        };
    }
    static <From, To> Function<List<Boolean>, List<To>> applyListBooleans(List<IPartialFunction<From, To>> pfns, From from) {
        return applyListBooleans(pfns, from, true);
    }
    static <From, To> Function<List<Boolean>, List<To>> applyListBooleans(List<IPartialFunction<From, To>> pfns, From from, boolean throwExceptionIfNotDefined) {
        return booleans -> {
            if (pfns.size() != booleans.size())
                throw new IllegalArgumentException("Software error: pfns and booleans must be the same size");
            List<To> result = new ArrayList<>();
            for (int i = 0; i < pfns.size(); i++)
                if (booleans.get(i)) {
                    var pfn = pfns.get(i);
                    if (pfn.isDefinedAt(from)) result.add(pfn.apply(from));
                    else {
                        if (throwExceptionIfNotDefined)
                            throw new IllegalArgumentException("Software error: pfn " + i + " not defined at " + from);
                    }
                }
            return result;
        };
    }

    static <From, To> Stream<List<To>> permutations(List<IPartialFunction<From, To>> pfns, From from) {
        var isOkToUseBooleans = IPartialFunction.isOkToUseBooleans(pfns);
        var applyListBooleans = IPartialFunction.applyListBooleans(pfns, from);
        return Permutations.permutate(pfns.size()).filter(isOkToUseBooleans).map(applyListBooleans);
    }
    static <From, To> void forEachPermutation(List<IPartialFunction<From, To>> pfns, From from, BiConsumer<List<Boolean>, List<To>> consumer) {
        forEachPermutation(pfns, from, consumer, true);
    }
    static <From, To> void forEachPermutation(List<IPartialFunction<From, To>> pfns, From from, BiConsumer<List<Boolean>, List<To>> consumer, boolean throwExceptionIfNotDefined) {
        var isOkToUseBooleans = IPartialFunction.isOkToUseBooleans(pfns);
        var applyListBooleans = IPartialFunction.applyListBooleans(pfns, from, throwExceptionIfNotDefined);
        Permutations.permutate(pfns.size()).filter(isOkToUseBooleans).forEach(booleans ->
                consumer.accept(booleans, applyListBooleans.apply(booleans)));
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
class PartialFunctionAlwaysTrue<From, To> extends PartialFunction<From, To> implements IPartialFunctionAlwaysTrue {
    public PartialFunctionAlwaysTrue(Predicate<From> isDefinedAt, Function<From, To> fn) {
        super(isDefinedAt, fn);
    }
}

class PartialFunctionIfThenElse<From, To> extends PartialFunction<From, To> implements IPartialFunctionAlwaysTrue {
    public PartialFunctionIfThenElse(Predicate<From> condition, Function<From, To> trueFn, Function<From, To> falseFn) {
        super(ignore -> true, from -> condition.test(from) ? trueFn.apply(from) : falseFn.apply(from));
    }
}