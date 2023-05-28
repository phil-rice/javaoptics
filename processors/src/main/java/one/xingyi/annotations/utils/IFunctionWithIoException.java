package one.xingyi.annotations.utils;

import java.io.IOException;
import java.util.function.Predicate;

public interface IFunctionWithIoException <From,To>{
    To apply(From from) throws IOException;

}
