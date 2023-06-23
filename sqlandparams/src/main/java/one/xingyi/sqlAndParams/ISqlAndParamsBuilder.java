package one.xingyi.sqlAndParams;

import one.xingyi.fp.IPartialFunction;

import java.util.function.Function;

import static one.xingyi.fp.Safe.safeString;

public interface ISqlAndParamsBuilder<Query> {
    default IPartialFunction<Query, SqlAndParams> always(Function<Query, SqlAndParams> fn) {
        return IPartialFunction.always(fn);
    }

    default <T> IPartialFunction<Query, ISqlAndParams> fieldInWhere(Function<Query, T> fieldFn, Function<Query, String> sqlFn) {
        return IPartialFunction.of(query -> fieldFn.apply(query) != null, query -> ISqlAndParams.postSql(sqlFn.apply(query), fieldFn.apply(query)));
    }
    default IPartialFunction<Query, ISqlAndParams> stringFieldInWhere(Function<Query, String> fieldFn, Function<Query, String> sqlFn) {
        return IPartialFunction.of(query -> safeString(fieldFn.apply(query)).length() > 0, query -> ISqlAndParams.postSql(sqlFn.apply(query), fieldFn.apply(query)));
    }
    default IPartialFunction<Query, ISqlAndParams> stringFieldInWhereUpperCaseAndTrim(Function<Query, String> fieldFn, Function<Query, String> sqlFn) {
        return IPartialFunction.of(query -> safeString(fieldFn.apply(query)).length() > 0, query -> ISqlAndParams.postSql(sqlFn.apply(query), fieldFn.apply(query).trim().toUpperCase()));
    }

}
