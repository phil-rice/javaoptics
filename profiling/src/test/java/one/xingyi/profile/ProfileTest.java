package one.xingyi.profile;

import lombok.var;
import one.xingyi.helpers.StringHelper;
import one.xingyi.interfaces.INanoTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static one.xingyi.helpers.StringHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfileTest {
    String emptyInfoJson = "{'count':0,'avg':0,'total':0,'<10ms':{'count':0,'total':0,'avg':0,'snapshot':0},'<100ms':{'count':0,'total':0,'avg':0,'snapshot':0},'<1s:':{'count':0,'total':0,'avg':0,'snapshot':0},'<10s':{'count':0,'total':0,'avg':0,'snapshot':0},'rest':{'count':0,'total':0,'avg':0,'snapshot':0}}";
    String childTwoCallsJson = toDoubleQuotes(
            "{'count':2,'avg':1,'total':2,'<10ms':{'count':2,'total':2,'avg':1,'snapshot':1}," +
                    "'<100ms':{'count':0,'total':0,'avg':0,'snapshot':0}," +
                    "'<1s:':{'count':0,'total':0,'avg':0,'snapshot':0}," +
                    "'<10s':{'count':0,'total':0,'avg':0,'snapshot':0}," +
                    "'rest':{'count':0,'total':0,'avg':0,'snapshot':0}}"
    );

    @Test
    void testEmpty() {
        Profile profile = getProfile();
        var info = profile.info();
        assertEquals(0, info.getCount());
        assertEquals(0, info.getTotalMs());
        assertEquals(0, info.getSnapshotMs());
        assertEquals("", info.getPath());
        assertEquals(0, info.getSnapshotMs());
        assertEquals("{'':" + emptyInfoJson + "}", removeWhiteSpace(toSingleQuotes(profile.json())));
    }

    @Test
    void testChild() {
        Profile profile = getProfile();
        Profile child = (Profile) profile.child("a").child("b").child("c");
        child.run(() -> {});
        child.run(() -> {});
        Assertions.assertEquals(new ProfileInfo("", 0, 0, 0), profile.info());

        var info = child.info();
        assertEquals(new ProfileInfo("a.b.c", 2, 2, 1), info);
    }
    @Test
    void testJson() {
        Profile profile = getProfile();
        Profile child = (Profile) profile.child("a").child("b").child("c");
        child.run(() -> {});
        child.run(() -> {});
        assertEquals(toDoubleQuotes("{'a.b.c':" + childTwoCallsJson + "}"), removeWhiteSpace(child.json()));
        assertEquals(toDoubleQuotes("{'':" + emptyInfoJson + ",'a':" + emptyInfoJson + ",'a.b':" + emptyInfoJson + ",'a.b.c':" + childTwoCallsJson + "}"), removeWhiteSpace(profile.json()));
    }
    private static Profile getProfile() {
        return (Profile) IProfile.root("pck", INanoTime.testNanoTime());
    }

}