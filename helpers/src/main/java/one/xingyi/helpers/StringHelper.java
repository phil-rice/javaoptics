package one.xingyi.helpers;

import lombok.var;

import java.util.Arrays;
import java.util.function.Function;

public interface StringHelper {

    static String quoteIfNeeded(Object o) {
        if (Number.class.isAssignableFrom(o.getClass())) return o.toString();
        return '"' + o.toString() + '"';
    }

    static String quoteIfNeededForJson(Object o) {
        if (Number.class.isAssignableFrom(o.getClass())) return o.toString();
        var s = o.toString();
        if (s.startsWith("{") && s.endsWith("}")) return s;
        return '"' + o.toString() + '"';
    }

    static String toSingleQuotes(String s) {
        return s.replaceAll("\"", "'");
    }
    static String toDoubleQuotes(String s) {
        return s.replaceAll("'", "\"");
    }
    static String removeWhiteSpace(String s) {
        return s.replaceAll("\\s", "");
    }

    public static String sanitizeForObjectName(String url) {
        return url.replaceAll("[^a-zA-Z0-9]", "_");
    }
    static String toJsonObject(Object... avs) {
        return "{" + toAttributeValue(",", avs) + "}";
    }

    static String toAttributeValue(String separator, Object... avs) {
        if (avs.length % 2 != 0)
            throw new RuntimeException("Odd number of attribute values" + avs.length + "\n" + Arrays.asList(avs));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < avs.length; i += 2) {
            if (sb.length() > 0) sb.append(separator);
            sb.append('"');
            sb.append(avs[i]);
            sb.append("\":");
            sb.append(quoteIfNeededForJson(avs[i + 1]));
        }
        return sb.toString();
    }

    static Function<String, String> quote(String open, String close) {
        return s -> open + s + close;
    }

    Function<String, String> doubleQuote = quote("\"", "\"");
    Function<String, String> brackets = quote("{", "}");

}
