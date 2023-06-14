package one.xingyi.fp;

import one.xingyi.interfaces.FunctionWithException;

import java.util.HashMap;
import java.util.Map;

public interface Maps {
    static <K, V1, V2> Map<K, V2> map(Map<K, V1> map, FunctionWithException<V1, V2> fn) throws Exception {
        Map<K, V2> result = new HashMap<>();
        for (K k : map.keySet()) {
            result.put(k, fn.apply(map.get(k)));
        }
        return result;
    }

}
