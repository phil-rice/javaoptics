package one.xingyi.profile;

import one.xingyi.helpers.NumberHelpers;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static one.xingyi.helpers.StringHelper.toJsonObject;

public class ProfileBucket {
    static ProfileBucket simple() {
        return new ProfileBucket();
    }

    final AtomicInteger count = new AtomicInteger();
    final AtomicLong total = new AtomicLong();
    long snapshot = 0l;

    public void clear() {
        count.set(0);
        total.set(0);
    }

    public void add(long time) {
        snapshot = time;
        count.incrementAndGet();
        total.addAndGet(time);
    }


    public int getCount() {
        return count.get();
    }

    public long getTotal() {
        return total.get();
    }

    public long getSnapshot() {
        return snapshot;
    }

    @Override
    public String toString() {
        int nanosToMs = 1000000;
        int c = count.get();
        long totalL = total.get() / nanosToMs;
        long avg = NumberHelpers.avg(totalL, c);
        return toJsonObject("count", c, "total", totalL, "avg", avg , "snapshot", snapshot / nanosToMs);
    }
}
