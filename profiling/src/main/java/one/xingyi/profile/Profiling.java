package one.xingyi.profile;

import one.xingyi.interfaces.INanoTime;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Profiling implements ProfilingMBean {
    IProfile profile = IProfile.makeProfileMap(INanoTime.realNanoTime);

    public String getProfiles() {
        return profile.print();
    }
    public int getCounts() {
        return 999;
    }

    public void doSomething(){
        System.out.println("I did something");
    }

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        Profiling mBean = new Profiling();


        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("one.xingyi.profiler:type=Profile");
        mbs.registerMBean(mBean, name);














        while (true) {
            Thread.sleep(1000);
            mBean.profile.add("test", 100);
            mBean.profile.add("test2", 1000);
        }
    }
}
