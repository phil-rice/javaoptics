package one.xingyi.profile;

import lombok.var;
import one.xingyi.helpers.ListHelpers;
import one.xingyi.interfaces.INanoTime;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface AddProfilingChild {
    void addChild(Profiling profiling);
}

public class Profiling implements ProfilingMBean, AddProfilingChild {
    final ProfileImpl profile;
    final Profiling parent;
    final static List<Profiling> children = Collections.synchronizedList(new ArrayList<>());

    public String printAll() {
        return "[" + String.join(",\n", ListHelpers.collect(children, p -> p.size() > 0, p -> p.profile.print())) + "]";
    }

    private int size() {
        return profile.map.size();
    }


    private INanoTime nanoTime;
    final String packageName;

    public Profiling(INanoTime nanoTime, String packageName) {
        this(nanoTime, packageName, null);
    }

    public Profiling(INanoTime nanoTime, String packageName, Profiling parent) {
        this.nanoTime = nanoTime;
        this.packageName = packageName;
        this.parent = parent;
        profile = (ProfileImpl) IProfile.makeProfileMap(nanoTime).withPrefix(packageName);
        children.add(this);
    }

    public IProfileBuilder registerWithPrefix(String prefix) {
        var mBean = new Profiling(nanoTime, packageName, this);
        mBean.register(prefix);
        return mBean.profile;
    }

    public IProfileBuilder registerWithPrefixAndMain(String prefix, String main) {
        return registerWithPrefix(prefix).main(main);
    }

    @Override
    public String print() {
        return profile.print();
    }

    public void clear() {
        profile.clear();
    }

    public void register(String name) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            mbs.registerMBean(this, new ObjectName(packageName + ":type=Profile,name=" + name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        Profiling mBean = new Profiling(INanoTime.realNanoTime, "one.xingyi");
        IProfile p1 = mBean.registerWithPrefixAndMain("op", "test11");
        IProfile p2 = mBean.registerWithPrefixAndMain("two", "test21");

        while (true) {
            Thread.sleep(1000);
            p1.add("test11", 100000000L);
            p1.add("test12", 100000000L);
            p2.add("test21", 100000000L);
            p2.add("test22", 100000000L);
        }
    }

    IProfileInfo mainProfileInfo() {
        return profile.mainProfileInfo();
    }

    @Override
    public int getCount() {
        return mainProfileInfo().getCount();
    }

    @Override
    public long getTotal() {
        return mainProfileInfo().getTotal() / 1000000;
    }

    @Override
    public long getSnapshot() {
        return mainProfileInfo().getSnapshot() / 1000000;
    }

    @Override
    public String getMain() {
        return profile.prefix + profile.main;
    }

    @Override
    public void addChild(Profiling profiling) {
        if (parent != null) parent.addChild(profiling);
        else children.add(profiling);
    }
}
