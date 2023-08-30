package one.xingyi.profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractProfileBucketTest {

    abstract IProfileBucket makeBucket();

    @Test
    void testSnapshot() {
        IProfileBucket bucket = makeBucket();
        bucket.add(1);
        assertEquals(1, bucket.snapshot());
        bucket.add(2);
        assertEquals(2, bucket.snapshot());
        bucket.add(3);
        assertEquals(3, bucket.snapshot());
    }

    @Test
    void testCount() {
        IProfileBucket bucket = makeBucket();
        assertEquals(0, bucket.count());
        bucket.add(1);
        assertEquals(1, bucket.count());
        bucket.add(1);
        assertEquals(2, bucket.count());
        bucket.add(1);
        assertEquals(3, bucket.count());
    }

    @Test
    void testTotal() {
        IProfileBucket bucket = makeBucket();
        assertEquals(0, bucket.total());
        bucket.add(1);
        assertEquals(1, bucket.total());
        bucket.add(2);
        assertEquals(3, bucket.total());
        bucket.add(3);
        assertEquals(6, bucket.total());
    }

    @Test
    void testAvg() {
        IProfileBucket bucket = makeBucket();
        assertEquals(0, bucket.avg());
        bucket.add(1);
        assertEquals(1, bucket.avg());
        bucket.add(2);
        assertEquals(1, bucket.avg());
        bucket.add(3);
        assertEquals(2, bucket.avg());
    }
}

class ProfileBucketTest extends AbstractProfileBucketTest {
    @Override
    IProfileBucket makeBucket() {
        return new ProfileBucket();
    }
}