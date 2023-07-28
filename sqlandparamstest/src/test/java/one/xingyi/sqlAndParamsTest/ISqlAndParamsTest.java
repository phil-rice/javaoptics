package one.xingyi.sqlAndParamsTest;

import lombok.val;
import net.sf.jsqlparser.JSQLParserException;
import one.xingyi.sqlAndParams.ISqlAndParams;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ISqlAndParamsTestTest {

    @Test
    void testChecksForLegalSqlUnhappyCases() throws JSQLParserException {
        assertEquals("Empty sql", assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlLegal("")).getMessage());
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from from"));
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlLegal("select '1 from dual"));
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from dual*"));
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from dual where"));
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlLegal("select 1 from dual where 1=1 and"));
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
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select * from", "")));
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("", "table")));
        assertThrows(JSQLParserException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select * from", "where 1=1")));
    }

    @Test
    void testIsLegalSqlAndParamsMismatchedQuestionsMarks() throws JSQLParserException {
        assertThrows(SqlAndParamsMismatchException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select 1 from dual where ?=1", "")));
        assertThrows(SqlAndParamsMismatchException.class, () -> ISqlAndParamsTest.checkSqlAndParamsLegal(ISqlAndParams.of("select 1 from dual where ?=1 and ?=2", "", "1")));
    }


}