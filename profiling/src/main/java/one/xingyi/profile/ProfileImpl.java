package one.xingyi.profile;

import one.xingyi.helpers.MapHelpers;
import one.xingyi.helpers.StringHelper;
import one.xingyi.interfaces.INanoTime;
import one.xingyi.interfaces.RunnableWithExceptionE;
import one.xingyi.interfaces.SupplierWithExceptionE;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

class ProfileImpl implements IProfile, IProfileBuilder, IProfileDetailedInfo, IProfileControl {
    final ConcurrentHashMap<String, ProfileBuckets<ProfileBucket>> map;
    final String prefix;
    final INanoTime nanoTime;
    long mainSnapshot = 0;
    String main;

    public ProfileImpl(String prefix, ConcurrentHashMap<String, ProfileBuckets<ProfileBucket>> map, INanoTime nanoTime) {
        this.prefix = prefix;
        this.map = map;
        this.nanoTime = nanoTime;
    }

    public IProfileBuilder main(String name) {
        main = name;
        return this;
    }

    public IProfileBuilder withPrefix(String prefix) {
        return new ProfileImpl(this.prefix + prefix + ".", map, nanoTime);
    }

    static IProfileInfo fromProfileBuckets(ProfileBuckets<ProfileBucket> bs, long mainSnapshot) {
        return new IProfileInfo() {
            @Override
            public int getCount() {
                return bs.mapAndAdd(pb -> pb.count.get(), Integer::sum);
            }

            @Override
            public long getTotal() {
                return bs.mapAndAdd(pb -> pb.total.get(), Long::sum);
            }

            @Override
            public long getSnapshot() {
                return mainSnapshot;
            }
        };
    }

    @Override
    public IProfileInfo mainProfileInfo() {
        return fromProfileBuckets(getBucketAddingIfNeeded(prefix + main), mainSnapshot);
    }

    final static int nanosToMs = 1000000;

    @Override
    public String print() {
        return MapHelpers.jsonPrint(",\n", map, (k, v) -> {
            long time = v.mapAndAdd(pb -> pb.total.get(), Long::sum) / nanosToMs;
            int count = v.mapAndAdd(pb -> pb.count.get(), Integer::sum);
            long avg = count == 0 ? 0 : time / count;
            return StringHelper.toJsonObject(
                    "count", count,
                    "time", time,
                    "avg", avg,
                    "<10ms", v.lessThan10ms,
                    "<100ms", v.lessThan100ms,
                    "<1s", v.lessThan1s,
                    "<10s", v.lessThan10s,
                    "rest", v.rest);
        });
    }

    @Override
    public <T, E extends Exception> T profileE(String name, SupplierWithExceptionE<T, E> fn) throws E {
        long start = nanoTime.nanoTime();
        try {
            return fn.get();
        } finally {
            add(name, nanoTime.nanoTime() - start);
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
        long start = nanoTime.nanoTime();
        try {
            fn.run();
        } finally {
            add(name, nanoTime.nanoTime() - start);
        }
    }

    @Override
    public <E extends Exception> void runE(String name, RunnableWithExceptionE<E> fn) throws E {
        long start = nanoTime.nanoTime();
        try {
            fn.run();
        } finally {
            add(name, nanoTime.nanoTime() - start);
        }

    }

    @Override
    public void add(String name, long duration) {
        String fullName = prefix + name;
        if (name.equals(main)) mainSnapshot = duration;
        ProfileBuckets<ProfileBucket> bucket = getBucketAddingIfNeeded(fullName);
        ProfileBuckets.add(bucket, duration);
    }

    private ProfileBuckets<ProfileBucket> getBucketAddingIfNeeded(String fullName) {
        boolean isIn = map.containsKey(fullName);
        ProfileBuckets<ProfileBucket> bucket = map.getOrDefault(fullName, ProfileBuckets.create());
        if (!isIn) map.put(fullName, bucket);
        return bucket;
    }

    @Override
    public Map<String, ProfileBuckets<Long>> getMs() {
        return MapHelpers.map(map, (k, v) -> v.map(b -> b.getAvg() / 1000000));
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

    @Override
    public void clear() {
        map.clear();
    }
}
