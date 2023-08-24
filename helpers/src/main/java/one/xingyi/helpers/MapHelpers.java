package one.xingyi.helpers;

import one.xingyi.interfaces.FunctionWithExceptionE;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface MapHelpers {
    static <K, V, V1> Map<K, V1> map(Map<K, V> map, BiFunction<K, V, V1> fn) {
        Map<K, V1> result = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet())
            result.put(entry.getKey(), fn.apply(entry.getKey(), entry.getValue()));
        return result;
    }

    static <V> String print(Map<String, V> map, BiFunction<String, V, String> fn) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        int maxLength = list.stream().mapToInt(String::length).max().orElse(0);
        for (String key : list) {
            if (sb.length() > 0) sb.append("\n");
            sb.append(String.format("%-" + maxLength + "s", key));
            sb.append(" ");
            sb.append(fn.apply(key, map.get(key)));
        } return sb.toString();
    }
}
