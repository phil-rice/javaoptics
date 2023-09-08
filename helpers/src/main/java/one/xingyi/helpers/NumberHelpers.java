package one.xingyi.helpers;

public interface NumberHelpers {
    static long avg(long total, int count) {
        return count == 0 ? 0 : total / count;
    }
    static int avg(int total, int count) {
        return count == 0 ? 0 : total / count;
    }

}
