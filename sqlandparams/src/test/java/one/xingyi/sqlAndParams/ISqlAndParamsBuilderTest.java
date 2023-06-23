package one.xingyi.sqlAndParams;

import lombok.*;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
class RequestForTest {
    final String some;
    final String data;
}

class FrTest implements ISqlAndParamsBuilder<RequestForTest> {

}
class ISqlAndParamsBuilderTest implements ISqlAndParamsBuilder<RequestForTest> {

    RequestForTest req = new RequestForTest("some", "data");
    RequestForTest reqWithSpacesAsWell = new RequestForTest("  some  ", "data");
    RequestForTest reqWithEmptyStrings = new RequestForTest("", "");
    RequestForTest reqWithNulls = new RequestForTest(null, null);
    Function<RequestForTest, SqlAndParams> sqlParamFn = req -> ISqlAndParams.preSql(req.some + req.data);
    @Test
    void testAlways() {
        val fn = always(sqlParamFn);
        assertTrue(fn.isDefinedAt(req));
        assertTrue(fn.isDefinedAt(reqWithNulls));
        assertEquals(ISqlAndParams.preSql("somedata"), fn.apply(req));

    }
    @Test
    void testFieldInWhere() {
        val fn = fieldInWhere(RequestForTest::getSome, req -> req.some + req.data);
        assertTrue(fn.isDefinedAt(req));
        assertFalse(fn.isDefinedAt(reqWithNulls));
        assertTrue(fn.isDefinedAt(reqWithEmptyStrings));
        assertEquals(ISqlAndParams.postSql("somedata", "some"), fn.apply(req));
    }
    @Test
    void testStringFieldInWhere() {
        val fn = stringFieldInWhere(RequestForTest::getSome, req -> req.some + req.data);
        assertTrue(fn.isDefinedAt(req));
        assertFalse(fn.isDefinedAt(reqWithNulls));
        assertFalse(fn.isDefinedAt(reqWithEmptyStrings));
        assertEquals(ISqlAndParams.postSql("somedata", "some"), fn.apply(req));
    }
    @Test
    void testStringFieldInWhereUpperCaseAndTrim() {
        val fn = stringFieldInWhereUpperCaseAndTrim(RequestForTest::getSome, req -> req.some.trim() + req.data.trim());
        assertTrue(fn.isDefinedAt(req));
        assertTrue(fn.isDefinedAt(reqWithSpacesAsWell));
        assertFalse(fn.isDefinedAt(reqWithNulls));
        assertFalse(fn.isDefinedAt(reqWithEmptyStrings));
        assertEquals(ISqlAndParams.postSql("somedata", "SOME"), fn.apply(req));
        assertEquals(ISqlAndParams.postSql("somedata", "SOME"), fn.apply(reqWithSpacesAsWell));
    }

}