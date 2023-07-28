package one.xingyi.sqlAndParams;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import one.xingyi.fp.IPartialFunction;

import java.util.*;
import java.util.function.Function;

import static one.xingyi.fp.Safe.safeString;


public interface ISqlAndParams {
    String getPreSql();
    String getPostSql();
    List<Object> getParams();

    default public String getFullSql() {return getPreSql() + " " + getPostSql();}

    static ISqlAndParams preSql(String sql, Object... params) {
        return new SqlAndParams(sql, "", Arrays.asList(params));
    }
    static ISqlAndParams of(String preSql, String postSql, Object... params) {
        return new SqlAndParams(preSql, postSql, Arrays.asList(params));
    }
    static ISqlAndParams postSql(String sql, Object... params) {
        return new SqlAndParams("", sql, Arrays.asList(params));
    }
    static ISqlAndParams merge(List<ISqlAndParams> sqlAndParams) {
        StringBuilder preSql = new StringBuilder();
        StringBuilder postSql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        for (ISqlAndParams sap : sqlAndParams) {
            if (preSql.length() > 0) preSql.append(' ');
            preSql.append(sap.getPreSql().trim());
            if (postSql.length() > 0) postSql.append(' ');
            postSql.append(sap.getPostSql().trim());
            params.addAll(sap.getParams());
        }
        return new SqlAndParams(preSql.toString(), postSql.toString(), params);
    }

    static <Query> IPartialFunction<Query, ISqlAndParams> always(Function<Query, ISqlAndParams> fn) {
        return IPartialFunction.always(fn);
    }

    static <Query, T> IPartialFunction<Query, ISqlAndParams> fieldInWhere(Function<Query, T> fieldFn, Function<Query, String> sqlFn) {
        return IPartialFunction.of(query -> fieldFn.apply(query) != null, query -> ISqlAndParams.postSql(sqlFn.apply(query), fieldFn.apply(query)));
    }
    static <Query> IPartialFunction<Query, ISqlAndParams> stringFieldInWhere(Function<Query, String> fieldFn, Function<Query, String> sqlFn) {
        return IPartialFunction.of(query -> safeString(fieldFn.apply(query)).length() > 0, query -> ISqlAndParams.postSql(sqlFn.apply(query), fieldFn.apply(query)));
    }
    static <Query> IPartialFunction<Query, ISqlAndParams> stringFieldInWhereUpperCaseAndTrim(Function<Query, String> fieldFn, Function<Query, String> sqlFn) {
        return IPartialFunction.of(query -> safeString(fieldFn.apply(query)).length() > 0, query -> ISqlAndParams.postSql(sqlFn.apply(query), fieldFn.apply(query).trim().toUpperCase()));
    }

}

@ToString
@Getter
@EqualsAndHashCode
class SqlAndParams implements ISqlAndParams {
    public final String preSql;
    public final String postSql;
    public final List<Object> params;
    public SqlAndParams(String preSql, String postSql, List<Object> params) {
        this.preSql = preSql.trim();
        this.postSql = postSql.trim();
        this.params = Collections.unmodifiableList(params);
    }

}