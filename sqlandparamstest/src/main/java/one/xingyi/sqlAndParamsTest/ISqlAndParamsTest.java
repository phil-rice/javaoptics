package one.xingyi.sqlAndParamsTest;

import lombok.SneakyThrows;
import lombok.var;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import one.xingyi.fp.IPartialFunction;
import one.xingyi.fp.IPartialFunctionE;
import one.xingyi.helpers.Permutations;
import one.xingyi.sqlAndParams.ISqlAndParams;

import java.util.List;

public interface ISqlAndParamsTest {

    static void checkSqlLegal(String sql) throws MalformedSqlException {
        if (sql.trim().length() == 0) throw MalformedSqlException.emptySql();
        try {
            CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new MalformedSqlException(sql, e);
        }

    }

    static void checkParamsCountAgainstSql(ISqlAndParams sqlAndParams) {
        String sql = sqlAndParams.getFullSql();
        long count = sql.chars().filter(c -> c == '?').count();
        int paramsCount = sqlAndParams.getParams().size();
        if (count != paramsCount)
            throw new SqlAndParamsMismatchException("SqlAndParams mismatch. Count of ?s " + count + " and params " + paramsCount + "\nSql: " + sql + "\nparams: " + sqlAndParams.getParams());
    }


    static void checkSqlAndParamsLegal(ISqlAndParams sqlAndParams) {
        String sql = sqlAndParams.getFullSql();
        checkSqlLegal(sql);
        checkParamsCountAgainstSql(sqlAndParams);
    }

    static <Req> void checkSqlAndParamsPfnListLegal(Req req, List<IPartialFunction<Req, ISqlAndParams>> pfnList) throws JSQLParserException {
        ISqlAndParams merged = IPartialFunction.mapReduceFn(pfnList, ISqlAndParams::merge).apply(req);
        checkSqlAndParamsLegal(merged);
    }

    static <Req> void testPermutations(Req req, List<IPartialFunction<Req, ISqlAndParams>> pfnList) {
        testPermutations(req, pfnList, true);
    }

    static <Req> void testPermutations(Req req, List<IPartialFunction<Req, ISqlAndParams>> pfnList, boolean throwExceptionIfNotDefined) {
        IPartialFunction.forEachPermutation(pfnList, req, (booleans, list) -> {
            var merged = ISqlAndParams.merge(list);
            try {
                checkSqlAndParamsLegal(merged);
            } catch (Exception e) {
                throw new RuntimeException("Failed for " + booleans + " " + merged, e);
            }
        }, throwExceptionIfNotDefined);
    }
}
