package one.xingyi.profile;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static one.xingyi.helpers.StringHelper.toJsonObject;

public class ProfileBucket implements IProfileBucket {
    final AtomicInteger count = new AtomicInteger();
    final AtomicLong total = new AtomicLong();
    long snapshot = 0l;

    @Override
    public void add(long time) {
        snapshot = time;
        count.incrementAndGet();
        total.addAndGet(time);
    }


    @Override
    public int getCount() {
        return count.get();
    }

    @Override
    public long getTotal() {
        return total.get();
    }

    @Override
    public long getSnapshot() {
        return snapshot;
    }

    @Override
    public String toString() {
        int nanosToMs = 1000000;
        int c = count.get();
        long totalL = total.get() / nanosToMs;
        return toJsonObject("count", c, "total", totalL, "avg", getAvg()/nanosToMs, "snapshot", snapshot / nanosToMs);
    }
}
