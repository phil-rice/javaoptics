package one.xingyi.sqlAndParams;

import lombok.val;
import lombok.var;
import one.xingyi.fp.IPartialFunction;
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

    @Test
    void testMergeLists() {
        var list1 = Arrays.asList(IPartialFunction.<String, ISqlAndParams>always(query -> new SqlAndParams(query + "pre1", query + "post1", Arrays.asList(query + "_a", "b"))));
        var list2 = Arrays.asList(IPartialFunction.<String, ISqlAndParams>always(query -> new SqlAndParams(query + "pre2", query + "post2", Arrays.asList(query + "_c", "d"))));
        var list3 = Arrays.asList(IPartialFunction.<String, ISqlAndParams>always(query -> new SqlAndParams(query + "pre3", query + "post3", Arrays.asList(query + "_e", "f"))));

        var actual = ISqlAndParams.<String>mergeLists(Arrays.asList(list1, list2, list3));
        assertEquals(ISqlAndParams.preSql("qpre1 qpost1 qpre2 qpost2 qpre3 qpost3", "q_a", "b", "q_c", "d", "q_e", "f"), actual.apply("q"));


    }
}