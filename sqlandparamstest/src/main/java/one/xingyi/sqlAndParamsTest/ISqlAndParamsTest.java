package one.xingyi.sqlAndParamsTest;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import one.xingyi.sqlAndParams.ISqlAndParams;

public interface ISqlAndParamsTest {

    static void checkSqlLegal(String sql) throws JSQLParserException {
        if (sql.trim().length() == 0) throw new JSQLParserException("Empty sql");
        CCJSqlParserUtil.parse(sql);
    }
    static void checkParamsCountAgainstSql(ISqlAndParams sqlAndParams) {
        String sql = sqlAndParams.getFullSql();
        long count = sql.chars().filter(c -> c == '?').count();
        int paramsCount = sqlAndParams.getParams().size();
        if (count != paramsCount)
            throw new SqlAndParamsMismatchException("SqlAndParams mismatch. Count of ?s " + count + " and params " + paramsCount + "\nSql: " + sql + "\nparams: " + sqlAndParams.getParams());
    }

    static void checkSqlAndParamsLegal(ISqlAndParams sqlAndParams) throws JSQLParserException {
        String sql = sqlAndParams.getFullSql();
        checkSqlLegal(sql);
        checkParamsCountAgainstSql(sqlAndParams);
    }
}
