package one.xingyi.profile;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import one.xingyi.helpers.NumberHelpers;
import one.xingyi.helpers.StringHelper;

import java.util.function.BinaryOperator;
import java.util.function.Function;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ProfileBuckets<T> {
    private long snapshot;
    public final T lessThan10ms;
    public final T lessThan100ms;
    public final T lessThan1s;
    public final T lessThan10s;
    public final T rest;

    public long snapshot() {
        return snapshot;
    }
    public String toString() {
        return StringHelper.toJsonObject(
                "<10ms", lessThan10ms,
                "<100ms", lessThan100ms,
                "<1s:", lessThan1s,
                "<10s", lessThan10s,
                "rest", rest);
    }
    public static String json(ProfileBuckets<ProfileBucket> bs) {
        int count = bs.mapAndAdd(ProfileBucket::getCount, Integer::sum);
        long total = bs.mapAndAdd(ProfileBucket::getTotal, Long::sum) / 1000000;
        long avg = NumberHelpers.avg(total, count);
        return StringHelper.toJsonObject(
                "count", count,
                "avg", avg,
                "total", total,
                "<10ms", bs.lessThan10ms,
                "<100ms", bs.lessThan100ms,
                "<1s:", bs.lessThan1s,
                "<10s", bs.lessThan10s,
                "rest", bs.rest);

    }
    public static ProfileBuckets<ProfileBucket> create() {
        return new ProfileBuckets<>(new ProfileBucket(), new ProfileBucket(), new ProfileBucket(), new ProfileBucket(), new ProfileBucket());
    }

    public static void add(ProfileBuckets<ProfileBucket> buckets, long time) {
        buckets.snapshot = time;
        if (time < 10000000L) buckets.lessThan10ms.add(time);
        else if (time < 100000000L) buckets.lessThan100ms.add(time);
        else if (time < 1000000000L) buckets.lessThan1s.add(time);
        else if (time < 10000000000L) buckets.lessThan10s.add(time);
        else buckets.rest.add(time);
    }
    public <T1> ProfileBuckets<T1> map(Function<T, T1> fn) {
        return new ProfileBuckets<>(fn.apply(lessThan10ms), fn.apply(lessThan100ms), fn.apply(lessThan1s), fn.apply(lessThan10s), fn.apply(rest));
    }
    public T add(BinaryOperator<T> add) {
        return add.apply(add.apply(add.apply(add.apply(lessThan10ms, lessThan100ms), lessThan1s), lessThan10s), rest);
    }
    public <Acc> Acc mapAndAdd(Function<T, Acc> fn, BinaryOperator<Acc> add) {
        return map(fn).add(add);
    }

    static public void clear(ProfileBuckets<ProfileBucket> b) {
        b.lessThan10ms.clear();
        b.lessThan100ms.clear();
        b.lessThan1s.clear();
        b.lessThan10s.clear();
        b.rest.clear();
    }

}
