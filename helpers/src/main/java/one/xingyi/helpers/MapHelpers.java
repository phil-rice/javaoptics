package one.xingyi.helpers;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MapHelpers {
    static <K, V, V1> Map<K, V1> map(Map<K, V> map, BiFunction<K, V, V1> fn) {
        Map<K, V1> result = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet())
            result.put(entry.getKey(), fn.apply(entry.getKey(), entry.getValue()));
        return result;
    }

    static <V> String jsonPrint(String separator, Map<String, V> map, BiFunction<String, V, String> fn) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        List<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        int maxLength = list.stream().mapToInt(String::length).max().orElse(0) + 2;
        for (String key : list) {
            if (sb.length() > 2) sb.append(separator);
            sb.append("  ");
            sb.append(String.format("%-" + maxLength + "s", StringHelper.doubleQuote.apply(key)));
            sb.append(":");
            sb.append(fn.apply(key, map.get(key)));
        }
        sb.append("\n}");
        return sb.toString();
    }

    static <K, V> V getOrAdd(Map<K, V> map, K key, Supplier< V> fn) {
        V result = map.get(key);
        if (result == null) {
            V value = fn.get();
            map.put(key, value);
            return value;
        }
        return result;
    }
}
