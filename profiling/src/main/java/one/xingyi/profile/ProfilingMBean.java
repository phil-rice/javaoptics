package one.xingyi.profile;

public interface ProfilingMBean extends IProfileInfo {

    String print();
    String printAll();

    void clear();

    String getMain();
}
