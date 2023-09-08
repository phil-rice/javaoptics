package one.xingyi.profile;

import lombok.RequiredArgsConstructor;
import one.xingyi.helpers.MapHelpers;
import one.xingyi.interfaces.INanoTime;
import one.xingyi.interfaces.RunnableWithExceptionE;
import one.xingyi.interfaces.SupplierWithExceptionE;
import one.xingyi.profile.pathmap.IPathMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Profile implements IProfile, IProfileInfo, ProfileMBean {
    final String jmxName;
    final INanoTime nanoTime;
    final IPathMap<ProfileBuckets<ProfileBucket>> map;
    @Override
    public <T, E extends Exception> T profile(SupplierWithExceptionE<T, E> fn) throws E {
        long start = nanoTime.nanoTime();
        try {
            return fn.get();
        } finally {
            long duration = nanoTime.nanoTime() - start;
            ProfileBuckets.add(map.get(), duration);
        }
    }
    @Override
    public <E extends Exception> void run(RunnableWithExceptionE<E> fn) throws E {
        long start = nanoTime.nanoTime();
        try {
            fn.run();
        } finally {
            long duration = nanoTime.nanoTime() - start;
            ProfileBuckets.add(map.get(), duration);
        }
    }
    @Override
    public IProfile child(String name) {return new Profile(jmxName, nanoTime, map.child(name));}
    @Override
    public IProfile registerMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            mbs.registerMBean(this, new ObjectName(jmxName + ":type=Profile,name=" + map.path()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this;
    }
    final static int nanosToMs = 1000000;

    @Override
    public int getCount() {
        return map.get().mapAndAdd(ProfileBucket::getCount, Integer::sum);
    }
    @Override
    public long getTotalMs() {
        return map.get().mapAndAdd(ProfileBucket::getTotal, Long::sum) / nanosToMs;
    }

    @Override
    public String getPath() {
        return map.path();
    }
    @Override
    public long getSnapshotMs() {
        return map.get().snapshot();
    }
    public IProfileInfo info() {
        return new ProfileInfo(map.path(), getCount(), getTotalMs(), map.get().snapshot() / nanosToMs);
    }

    public String json() {
        Map<String, String> map = new HashMap<>();
        this.map.fold(null, (acc, path, buckets) -> map.put(path, ProfileBuckets.json(buckets)));
        return MapHelpers.jsonPrint("\n", map, (key, value) -> value);
    }

    @Override
    public void clear() {
        ProfileBuckets.clear(map.get());
    }

}
