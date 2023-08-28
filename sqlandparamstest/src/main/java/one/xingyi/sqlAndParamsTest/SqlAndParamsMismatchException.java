package one.xingyi.sqlAndParamsTest;

public class SqlAndParamsMismatchException extends RuntimeException {
    private String sql;

    public SqlAndParamsMismatchException(String sql) {
        super("Malformed sql:\n" + sql);
        this.sql = sql;
    }
}
