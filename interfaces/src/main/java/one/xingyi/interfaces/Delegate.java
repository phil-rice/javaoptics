package one.xingyi.interfaces;

import java.util.function.Function;

public interface Delegate<From, To> extends Function<Function<From, To>, Function<From, To>> {
}
