package one.xingyi.fp;

import java.util.Collections;
import java.util.List;

public interface Safe {
    static String safeString(String s) {
        return s == null ? "" : s;
    }

    static <T> T safe(T t, T defValue) {
        return t == null ? defValue : t;
    }

    static <T> List<T> safeList(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
