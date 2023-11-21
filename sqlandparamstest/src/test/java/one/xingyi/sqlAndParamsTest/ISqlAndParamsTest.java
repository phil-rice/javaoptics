package one.xingyi.sqlAndParamsTest;

import lombok.*;
import net.sf.jsqlparser.JSQLParserException;
import one.xingyi.fp.IPartialFunction;
import one.xingyi.helpers.StringHelper;
import one.xingyi.sqlAndParams.ISqlAndParams;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static one.xingyi.helpers.StringHelper.removeWhiteSpace;
import static org.junit.jupiter.api.Assertions.*;

class ISqlAndParamsTestTest {

    @Test
    void testChecksForLegalSqlUnhappyCases() throws JSQLParserException {
        assertEquals("Empty sql", assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlLegal("")).getMessage());
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from from"));
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlLegal("select '1 from dual"));
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from dual*"));
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from dual where"));
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from dual where 1=1 and"));
    }
    @Test
    void testChecksForLegalSqlHappyCases() throws JSQLParserException {
        ISqlAndParamsTest.checkSqlLegal("select 1 from dual");
        ISqlAndParamsTest.checkSqlLegal("select * from friends");
        ISqlAndParamsTest.checkSqlLegal("select * from friends where 1=1 and 2=2");
    }

    @Test
    void testIsLegalSqlAndParamsHappyPath() throws JSQLParserException {
        ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select 1 from dual", ""));
        ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select 1 from dual", "where 1=1"));
        ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select 1 from dual where ?=1", "", 2));
        ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select * from", "aTable"));
        ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select * from table", "where 1=1"));
    }
    @Test
    void testIsLegalSqlAndParamsBadSql() {
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select * from", "")));
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("", "table")));
        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select * from", "where 1=1")));
    }

    @Test
    void testIsLegalSqlAndParamsMismatchedQuestionsMarks() throws JSQLParserException {
        assertThrows(SqlAndParamsMismatchException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select 1 from dual where ?=1", "")));
        assertThrows(SqlAndParamsMismatchException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select 1 from dual where ?=1 and ?=2", "", "1")));
    }

    @Test
    void testCheckLegalSqlGivesNiceMessage() {
        var e = assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlLegal("select * from"));
        assertEquals("MalformedSQL:select*from", removeWhiteSpace(e.getMessage()));
        assertEquals("select * from", e.sql);
    }
    @RequiredArgsConstructor
    @ToString
    @Getter
    @EqualsAndHashCode
    class RequestForTest {
        final String some;
        final String data;
    }

    RequestForTest reqEmpty = new RequestForTest(null, null);
    RequestForTest reqFull = new RequestForTest("some", "data");

    IPartialFunction<RequestForTest, ISqlAndParams> fn0Always =
            ISqlAndParams.always(req -> ISqlAndParams.of("select * from table1", "where true"));
    IPartialFunction<RequestForTest, ISqlAndParams> fn1 =
            ISqlAndParams.stringFieldInWhere(req -> req.some, req -> "and ?=some");

    IPartialFunction<RequestForTest, ISqlAndParams> brokenfn1 =
            ISqlAndParams.stringFieldInWhere(req -> req.some, req -> "?='some'");
    IPartialFunction<RequestForTest, ISqlAndParams> fn2 =
            ISqlAndParams.stringFieldInWhere(req -> req.data, req -> "and ?='data'");

    List<IPartialFunction<RequestForTest, ISqlAndParams>> list012 = Arrays.asList(fn0Always, fn1, fn2);
    List<IPartialFunction<RequestForTest, ISqlAndParams>> brokenlist = Arrays.asList(fn0Always, brokenfn1, fn2);
    @Test
    void testOnListOfPfns() throws JSQLParserException {
        ISqlAndParamsTest.checkSqlAndParamsPfnListLegal(reqEmpty, list012);
        ISqlAndParamsTest.checkSqlAndParamsPfnListLegal(reqFull, list012);

        assertThrows(MalformedSqlException.class, () -> ISqlAndParamsTest.checkSqlAndParamsPfnListLegal(reqFull, brokenlist));
    }

    @Test
    void testPermutations() {
        ISqlAndParamsTest.testPermutations(reqFull, list012);
        var e = assertThrows(RuntimeException.class, () -> ISqlAndParamsTest.testPermutations(reqFull, brokenlist));
        assertEquals("Failed for [true, true, false] SqlAndParams(preSql=select * from table1, postSql=where true ?='some', params=[some])", e.getMessage());
    }

}