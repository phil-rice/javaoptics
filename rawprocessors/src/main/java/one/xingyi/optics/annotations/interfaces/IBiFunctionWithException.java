package one.xingyi.optics.annotations.interfaces;

import java.io.IOException;

public interface IBiFunctionWithException<From1, From2, To> {
    To apply(From1 from1, From2 from2) throws IOException;
}
