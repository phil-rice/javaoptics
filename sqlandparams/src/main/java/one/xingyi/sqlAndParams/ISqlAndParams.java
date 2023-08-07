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
    static ISqlAndParams mergeWithPreAndPostSql(List<ISqlAndParams> sqlAndParams) {
    	StringBuilder preSql = new StringBuilder();
    	StringBuilder postSql = new StringBuilder();
    	List<Object> params = new ArrayList<>();
    	for (ISqlAndParams sap : sqlAndParams) {
    		if (preSql.length() > 0) preSql.append(' ');
    		params.addAll(sap.getParams());
    		if (postSql.length() > 0) postSql.append(' ');
    		params.addAll(sap.getParams());
    	}
    	return new SqlAndParams(preSql.toString(), postSql.toString(),params);
    }
    
    static ISqlAndParams mergeWithSql(List<ISqlAndParams> sqlAndParams) {
    	StringBuilder sql = new StringBuilder();
    	List<Object> params = new ArrayList<>();
    	for (ISqlAndParams sap : sqlAndParams) {
    		if (sql.length() > 0) sql.append(' ');
    		params.addAll(sap.getParams());
    	}
    	return new SqlAndParams(sql.toString(), params);
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
	public String sql;
    public String preSql;
    public String postSql;
    public final List<Object> params;
    public SqlAndParams(String preSql, String postSql, List<Object> params) {
        this.preSql = preSql.trim();
        this.postSql = postSql.trim();
        this.params = Collections.unmodifiableList(params);
    }
    public SqlAndParams(String sql, List<Object> params) {
    	this.sql = sql.trim();
    	this.params = Collections.unmodifiableList(params);
    }
    
	@Override
	public String getPreSql() {
		return this.preSql;
	}
	@Override
	public String getPostSql() {
		return this.postSql;
	}
	@Override
	public List<Object> getParams() {
		return this.params;
	}

}