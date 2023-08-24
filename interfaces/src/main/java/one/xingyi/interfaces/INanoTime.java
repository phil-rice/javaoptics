package one.xingyi.interfaces;

import java.util.concurrent.atomic.AtomicLong;

public interface INanoTime {
    long nanoTime();
    INanoTime realNanoTime = System::nanoTime;
    static INanoTime testNanoTime() {
        return new INanoTime() {
            private AtomicLong time = new AtomicLong(0L);
            @Override
            public long nanoTime() {
                return time.addAndGet(1000000L);
            }
        };
    }
}
