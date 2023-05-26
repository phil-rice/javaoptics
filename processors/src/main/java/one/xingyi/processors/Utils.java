package one.xingyi.processors;

public interface Utils {
    static String lastSegment(String s) {
        int i = s.lastIndexOf('.');
        return i == -1 ? s : s.substring(i + 1);
    }
}
