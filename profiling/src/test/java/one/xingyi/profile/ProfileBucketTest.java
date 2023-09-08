package one.xingyi.profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileBucketTest {

    private ProfileBucket makeBucket() {
        return new ProfileBucket();
    }

    @Test
    void testSnapshot() {
        ProfileBucket bucket = makeBucket();
        bucket.add(1);
        assertEquals(1, bucket.getSnapshot());
        bucket.add(2);
        assertEquals(2, bucket.getSnapshot());
        bucket.add(3);
        assertEquals(3, bucket.getSnapshot());
    }

    @Test
    void testCount() {
        ProfileBucket bucket = makeBucket();
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
        ProfileBucket bucket = makeBucket();
        assertEquals(0, bucket.getTotal());
        bucket.add(1);
        assertEquals(1, bucket.getTotal());
        bucket.add(2);
        assertEquals(3, bucket.getTotal());
        bucket.add(3);
        assertEquals(6, bucket.getTotal());
    }


}

