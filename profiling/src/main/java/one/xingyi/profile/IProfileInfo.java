package one.xingyi.profile;

public interface IProfileInfo {
    default long getAvg() {
        int n = getCount();
        long t = getTotal();
        return n == 0 ? 0 : t / n;
    }

    int getCount();

    long getTotal();

    long getSnapshot();
}
