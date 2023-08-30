package one.xingyi.profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractProfileBucketTest {

    abstract IProfileBucket makeBucket();

    @Test
    void testSnapshot() {
        IProfileBucket bucket = makeBucket();
        bucket.add(1);
        assertEquals(1, bucket.getSnapshot());
        bucket.add(2);
        assertEquals(2, bucket.getSnapshot());
        bucket.add(3);
        assertEquals(3, bucket.getSnapshot());
    }

    @Test
    void testCount() {
        IProfileBucket bucket = makeBucket();
        assertEquals(0, bucket.getCount());
        bucket.add(1);
        assertEquals(1, bucket.getCount());
        bucket.add(1);
        assertEquals(2, bucket.getCount());
        bucket.add(1);
        assertEquals(3, bucket.getCount());
    }

    @Test
    void testTotal() {
        IProfileBucket bucket = makeBucket();
        assertEquals(0, bucket.getTotal());
        bucket.add(1);
        assertEquals(1, bucket.getTotal());
        bucket.add(2);
        assertEquals(3, bucket.getTotal());
        bucket.add(3);
        assertEquals(6, bucket.getTotal());
    }

    @Test
    void testAvg() {
        IProfileBucket bucket = makeBucket();
        assertEquals(0, bucket.getAvg());
        bucket.add(1);
        assertEquals(1, bucket.getAvg());
        bucket.add(2);
        assertEquals(1, bucket.getAvg());
        bucket.add(3);
        assertEquals(2, bucket.getAvg());
    }
}

class ProfileBucketTest extends AbstractProfileBucketTest {
    @Override
    IProfileBucket makeBucket() {
        return new ProfileBucket();
    }
}