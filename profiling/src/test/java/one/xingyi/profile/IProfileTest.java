package one.xingyi.profile;

import one.xingyi.helpers.MapHelpers;
import one.xingyi.interfaces.INanoTime;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IProfileTest {
    long onems = 1000000L;
    long tenms = onems * 10;
    long oneHundredms = tenms * 10;
    long onesec = oneHundredms * 10;
    long tenSec = onesec * 10;

    @Test
    void testAddingSomeNumbersAndTheAvg1() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        addOneToEachBucket(p, "one", 1);
        addOneToEachBucket(p, "two", 1);
        Map<String, ProfileBuckets<Long>> expected = new HashMap<>();
        expected.put("one", new ProfileBuckets<>(1L, 10L, 100L, 1000L, 10000L));
        expected.put("two", new ProfileBuckets<>(1L, 10L, 100L, 1000L, 10000L));
        assertEquals(expected, p.getMs());
    }
    @Test
    void testAddingSomeNumbersAndTheAvg2() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        addOneToEachBucket(p, "one", 1);
        addOneToEachBucket(p, "one", 3);
        addOneToEachBucket(p, "two", 1);
        Map<String, ProfileBuckets<Long>> expected = new HashMap<>();
        expected.put("one", new ProfileBuckets<>(2L, 20L, 200L, 2000L, 20000L));
        expected.put("two", new ProfileBuckets<>(1L, 10L, 100L, 1000L, 10000L));
        assertEquals(expected, p.getMs());
    }

    @Test
    void testAddingSomeNumbersAndTheCounts1() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        addOneToEachBucket(p, "one", 1);
        addOneToEachBucket(p, "two", 1);
        Map<String, ProfileBuckets<Integer>> expected = new HashMap<>();
        expected.put("one", new ProfileBuckets<>(1, 1, 1, 1, 1));
        expected.put("two", new ProfileBuckets<>(1, 1, 1, 1, 1));
        assertEquals(expected, p.getCounts());
    }
    @Test
    void testAddingSomeNumbersAndTheCounts3() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        addOneToEachBucket(p, "one", 1);
        addOneToEachBucket(p, "two", 1);
        addOneToEachBucket(p, "one", 1);
        addOneToEachBucket(p, "one", 1);
        Map<String, ProfileBuckets<Integer>> expected = new HashMap<>();
        expected.put("one", new ProfileBuckets<>(3, 3, 3, 3, 3));
        expected.put("two", new ProfileBuckets<>(1, 1, 1, 1, 1));
        assertEquals(expected, p.getCounts());
    }

    @Test
    void testGetTotalAvg() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        addOneToEachBucket(p, "one", 1);
        addOneToEachBucket(p, "two", 1);
        addOneToEachBucket(p, "one", 3);
        Map<String, Long> expected = new HashMap<>();
        expected.put("one", 22222L);
        expected.put("two", 11111L);
        assertEquals(expected, p.getTotalAvg());

    }
    @Test
    void testGetTotalCounts() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        addOneToEachBucket(p, "one", 1);
        addOneToEachBucket(p, "two", 1);
        addOneToEachBucket(p, "one", 3);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("one", 10);
        expected.put("two", 5);
        assertEquals(expected, p.getTotalCounts());
    }
    @Test
    void testProfile() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        p.profile("one", () -> "hello");
        p.profile("one", () -> "hello");
        p.profile("two", () -> "hello");
        assertEquals("one {count: 2, time: 2 <10ms:{count=2,total=2,avg=1}, <100ms:{count=0,total=0,avg=0}, <1s:{count=0,total=0,avg=0}, <10s:{count=0,total=0,avg=0}, rest:{count=0,total=0,avg=0}}\n" +
                "two {count: 1, time: 1 <10ms:{count=1,total=1,avg=1}, <100ms:{count=0,total=0,avg=0}, <1s:{count=0,total=0,avg=0}, <10s:{count=0,total=0,avg=0}, rest:{count=0,total=0,avg=0}}", p.print());
    }
    @Test
    void testProfileE() {
        IProfile p = IProfile.makeProfileMap(INanoTime.testNanoTime());
        p.profileE("one", () -> "hello");
        p.profileE("one", () -> "hello");
        p.profileE("two", () -> "hello");
        assertEquals("one {count: 2, time: 2 <10ms:{count=2,total=2,avg=1}, <100ms:{count=0,total=0,avg=0}, <1s:{count=0,total=0,avg=0}, <10s:{count=0,total=0,avg=0}, rest:{count=0,total=0,avg=0}}\n" +
                "two {count: 1, time: 1 <10ms:{count=1,total=1,avg=1}, <100ms:{count=0,total=0,avg=0}, <1s:{count=0,total=0,avg=0}, <10s:{count=0,total=0,avg=0}, rest:{count=0,total=0,avg=0}}", p.print());
    }

    private void addOneToEachBucket(IProfile p, String name, int amt) {
        p.add(name, onems * amt);
        p.add(name, tenms * amt);
        p.add(name, oneHundredms * amt);
        p.add(name, onesec * amt);
        p.add(name, tenSec * amt);
    }
}