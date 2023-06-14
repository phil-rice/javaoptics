package one.xingyi.optics.annotations.interfaces;

import java.io.IOException;

public interface IFunctionWithException<From,To>{
    To apply(From from) throws IOException;

}
