package one.xingyi.helpers;

import java.util.ArrayList;
import java.util.List;

public interface ListHelpers {
    static <T> List<T> append(List<T> list, T t) {
        List<T> result = new ArrayList<>(list);
        result.add(t);
        return result;
    }
}
