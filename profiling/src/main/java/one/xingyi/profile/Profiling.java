package one.xingyi.profile;

import lombok.var;
import one.xingyi.helpers.ListHelpers;
import one.xingyi.interfaces.INanoTime;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class Profiling implements ProfilingMBean {
    final ProfileImpl profile;
    final static List<Profiling> allProfiling = new ArrayList<>();

    public static String printAll() {
        return String.join("\n", ListHelpers.map(allProfiling, p -> p.profile.print()));
    }

    private INanoTime nanoTime;
    final String packageName;

    public Profiling(INanoTime nanoTime, String packageName) {
        this.nanoTime = nanoTime;
        this.packageName = packageName;
        profile = (ProfileImpl) IProfile.makeProfileMap(nanoTime).withPrefix(packageName);
        allProfiling.add(this);
    }

    public IProfile registerWithPrefix(String prefix) {
        var mBean = new Profiling(nanoTime, packageName + "." + prefix);
        mBean.register();
        return mBean.profile;
    }

    @Override
    public String print() {
        return profile.print();
    }

    public void clear() {
        profile.clear();
    }

    public void register() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName(packageName + ":type=Profile");
            mbs.registerMBean(this, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        Profiling mBean = new Profiling(INanoTime.realNanoTime, "one.xingyi");
        IProfile p1 = mBean.registerWithPrefix("one");
        IProfile p2 = mBean.registerWithPrefix("two");

        while (true) {
            Thread.sleep(1000);
            p1.add("test11", 100);
            p1.add("test12", 100);
            p2.add("test21", 100);
            p2.add("test22", 100);
        }
    }

    IProfileInfo mainProfileInfo() {
        return profile.mainProfileInfo();
    }

    @Override
    public int count() {
        return mainProfileInfo().count();
    }

    @Override
    public long total() {
        return mainProfileInfo().total();
    }

    @Override
    public long snapshot() {
        return mainProfileInfo().snapshot();
    }
}
