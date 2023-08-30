package one.xingyi.profile;

public interface IProfileInfo {
    default long avg() {
        int n = count();
        long t = total();
        return n == 0 ? 0 : t / n;
    }

    int count();

    long total();

    long snapshot();
}
