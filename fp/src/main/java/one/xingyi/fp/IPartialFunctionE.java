package one.xingyi.fp;

import lombok.RequiredArgsConstructor;
import lombok.var;
import one.xingyi.interfaces.FunctionWithException;
import one.xingyi.interfaces.PredicateWithException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IPartialFunctionE<From, To> extends FunctionWithException<From, To> {
    boolean isDefinedAt(From from) throws Exception;

    default IPartialFunctionE<From, To> orElse(IPartialFunctionE<From, To> other) {
        return IPartialFunctionE.of(
                from -> isDefinedAt(from) || other.isDefinedAt(from),
                from -> isDefinedAt(from) ? apply(from) : other.apply(from));
    }

    static <From, To> IPartialFunctionE<From, To> always(FunctionWithException<From, To> fn) {
        return IPartialFunctionE.of(q -> true, fn);
    }
    static <From, To> To applyOrError(IPartialFunctionE<From, To> pfn, From from) throws Exception {
        if (pfn.isDefinedAt(from))
            return pfn.apply(from);
        throw new IllegalArgumentException("Not defined at " + from);
    }
    static <From, To> To applyOr(IPartialFunctionE<From, To> pfn, Supplier<To> defValue, From from) throws Exception {
        return pfn.isDefinedAt(from) ? pfn.apply(from) : defValue.get();
    }
    static <From, To> IPartialFunctionE<From, To> of(PredicateWithException<From> isDefinedAt, FunctionWithException<From, To> fn) {
        return new PartialFunctionE<>(isDefinedAt, fn);
    }
    static <From, To> IPartialFunctionE<From, To> notNull(FunctionWithException<From, To> fn) {
        return IPartialFunctionE.of(Objects::nonNull, fn);
    }
    static <From, To> FunctionWithException<From, List<To>> mapFn(Collection<IPartialFunctionE<From, To>> fns) {
        return from -> {
            var result = new ArrayList<To>();
            for (IPartialFunctionE<From, To> fn : fns)
                if (fn.isDefinedAt(from))
                    result.add(fn.apply(from));
            return result;
        };
    }
    static <From, To, Result> FunctionWithException<From, Result> mapReduceFn(Collection<IPartialFunctionE<From, To>> fns, FunctionWithException<List<To>, Result> reduceFn) {
        return from -> reduceFn.apply(mapFn(fns).apply(from));
    }
    static <From, To> FunctionWithException<From, To> chain(To defValue, Collection<IPartialFunctionE<From, To>> fns) {
        return from -> {
            for (IPartialFunctionE<From, To> fn : fns)
                if (fn.isDefinedAt(from))
                    return fn.apply(from);
            return defValue;
        };
    }
    static <From, To> IPartialFunctionE<From, To> chainToPfE(Collection<IPartialFunctionE<From, To>> fns) {
        return IPartialFunctionE.of(from -> {
            for (IPartialFunctionE<From, To> fn : fns)
                if (fn.isDefinedAt(from))
                    return true;
            return false;
        }, from -> {
            for (IPartialFunctionE<From, To> fn : fns)
                if (fn.isDefinedAt(from))
                    return fn.apply(from);
            throw new IllegalArgumentException("Software error: isDefined is not true for any partial function");
        });
    }
}
@RequiredArgsConstructor
class PartialFunctionE<From, To> implements IPartialFunctionE<From, To> {
    final PredicateWithException<From> isDefinedAt;
    final FunctionWithException<From, To> fn;
    @Override
    public boolean isDefinedAt(From from) throws Exception {
        return isDefinedAt.test(from);
    }
    @Override
    public To apply(From from) throws Exception {
        return fn.apply(from);
    }
}
