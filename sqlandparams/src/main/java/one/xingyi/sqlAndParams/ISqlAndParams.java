package one.xingyi.sqlAndParams;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;


public interface ISqlAndParams {


    static SqlAndParams preSql(String sql, Object... params) {
        return new SqlAndParams(sql, "", Arrays.asList(params));
    }
    static SqlAndParams of(String preSql, String postSql, Object... params) {
        return new SqlAndParams(preSql, postSql, Arrays.asList(params));
    }
    static SqlAndParams postSql(String sql, Object... params) {
        return new SqlAndParams("", sql, Arrays.asList(params));
    }
    static SqlAndParams merge(List<SqlAndParams> sqlAndParams) {
        StringBuilder preSql = new StringBuilder();
        StringBuilder postSql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        for (SqlAndParams sap : sqlAndParams) {
            if (preSql.length() > 0) preSql.append(' ');
            preSql.append(sap.preSql.trim());
            if (postSql.length() > 0) postSql.append(' ');
            postSql.append(sap.postSql.trim());
            params.addAll(sap.params);
        }
        return new SqlAndParams(preSql.toString(), postSql.toString(), params);
    }

}

@ToString
@Getter
@EqualsAndHashCode
class SqlAndParams implements ISqlAndParams {
    public final String preSql;
    public final String postSql;
    public final List<Object> params;
    public  SqlAndParams(String preSql, String postSql, List<Object> params) {
        this.preSql = preSql.trim();
        this.postSql = postSql.trim();
        this.params = Collections.unmodifiableList(params);
    }

}