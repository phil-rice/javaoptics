package one.xingyi.optics.annotations.utils;

import java.io.IOException;

public interface IFunctionWithIoException <From,To>{
    To apply(From from) throws IOException;

}
