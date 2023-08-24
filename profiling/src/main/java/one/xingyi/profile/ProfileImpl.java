package one.xingyi.profile;

import one.xingyi.helpers.MapHelpers;
import one.xingyi.interfaces.INanoTime;
import one.xingyi.interfaces.RunnableWithExceptionE;
import one.xingyi.interfaces.SupplierWithExceptionE;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

class ProfileImpl implements IProfile {
    private final ConcurrentHashMap<String, ProfileBuckets<ProfileBucket>> map;
    final INanoTime nanoTime;
    public ProfileImpl(ConcurrentHashMap<String, ProfileBuckets<ProfileBucket>> map, INanoTime nanoTime) {
        this.map = map;
        this.nanoTime = nanoTime;
    }
    @Override
    public String print() {
        int nanosToMs = 1000000;
        return MapHelpers.print(map, (k, v) -> "{count: " + v.mapAndAdd(pb -> pb.count.get(), Integer::sum) + ", time: " + v.mapAndAdd(pb->pb.total.get(), Long::sum)/nanosToMs +
                " " + "<10ms:" + v.lessThan10ms + ", <100ms:" + v.lessThan100ms + ", <1s:" + v.lessThan1s + ", <10s:" + v.lessThan10s + ", rest:" + v.rest + '}');
    }
    @Override
    public <T, E extends Exception> T profileE(String name, SupplierWithExceptionE<T, E> fn) throws E {
        long start = nanoTime.nanoTime(); try {
            return fn.get();
        } finally {
            add(name,  nanoTime.nanoTime() - start);
        }
    }
    @Override
    public <T> T profile(String name, Supplier<T> fn) {
        long start = nanoTime.nanoTime();
        try {
            return fn.get();
        } finally {
            add(name, nanoTime.nanoTime() - start);
        }
    }
    @Override
    public void run(String name, Runnable fn) {
        long start = nanoTime.nanoTime(); try {
            fn.run();
        } finally {
            add(name, nanoTime.nanoTime() - start);
        }
    }
    @Override
    public <E extends Exception> void runE(String name, RunnableWithExceptionE<E> fn) throws E {
        long start = nanoTime.nanoTime(); try {
            fn.run();
        } finally {
            add(name,  nanoTime.nanoTime() - start);
        }

    }
    @Override
    public void add(String name, long duration) {
        boolean isIn = map.contains(name);
        ProfileBuckets<ProfileBucket> bucket = map.getOrDefault(name, ProfileBuckets.create());
        ProfileBuckets.add(bucket, duration); if (!isIn) map.put(name, bucket);
    }

    @Override
    public Map<String, ProfileBuckets<Long>> getMs() {
        return MapHelpers.map(map, (k, v) -> v.map(b -> b.avg() / 1000000));
    }
    @Override
    public Map<String, ProfileBuckets<Integer>> getCounts() {
        return MapHelpers.map(map, (k, v) -> v.map(b -> b.count.get()));
    }
    @Override
    public Map<String, Integer> getTotalCounts() {
        return MapHelpers.map(getCounts(), (k, v) -> v.add(Integer::sum));
    }
    @Override
    public Map<String, Long> getTotalAvg() {
        return MapHelpers.map(getMs(), (k, v) -> v.add(Long::sum));
    }
}
