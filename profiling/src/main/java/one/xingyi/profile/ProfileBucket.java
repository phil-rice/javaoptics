package one.xingyi.profile;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileBucket {
    final AtomicInteger count = new AtomicInteger();
    final AtomicLong total = new AtomicLong();

    public void add(long time) {
        count.incrementAndGet(); total.addAndGet(time);
    }
    public long avg() { // Note that this might give slightly funny results given threading... if count and total are being spammed
        int n = count.get();
        long t = total.get();
        return n == 0 ? 0 : t / n;
    }
    @Override
    public String toString() {
        int nanosToMs = 1000000;
        return "{" + "count=" + count + ",total=" + total.get() / nanosToMs + ",avg=" + avg() / nanosToMs + '}';
    }
}
