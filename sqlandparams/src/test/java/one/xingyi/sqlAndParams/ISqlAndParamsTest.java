package one.xingyi.sqlAndParams;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ISqlAndParamsTest {


    @Test
    void testFactoryMethods() {
        assertEquals(new SqlAndParams("pre", "post", Arrays.asList("a", "b")), ISqlAndParams.of("pre", "post", "a", "b"));
        assertEquals(new SqlAndParams("pre", "", Arrays.asList("a", "b")), ISqlAndParams.preSql("pre", "a", "b"));
        assertEquals(new SqlAndParams("", "post", Arrays.asList("a", "b")), ISqlAndParams.postSql("post", "a", "b"));
    }

    @Test
    void testMerge() {
        val sp1 = new SqlAndParams("pre1", "post1", Arrays.asList("a", "b"));
        val sp2 = new SqlAndParams("pre2", "post2", Arrays.asList("c", "d"));
        val sp3 = new SqlAndParams("pre3", "post3", Arrays.asList("e", "f"));

        assertEquals(new SqlAndParams("pre1 pre2 pre3", "post1 post2 post3", Arrays.asList("a", "b", "c", "d", "e", "f")),
                ISqlAndParams.merge(Arrays.asList(sp1, sp2, sp3)));

    }
}