package one.xingyi.sqlAndParamsTest;

import net.sf.jsqlparser.JSQLParserException;

public class MalformedSqlException extends RuntimeException {
    public final String sql;

    public static MalformedSqlException emptySql() {
        return new MalformedSqlException("Empty sql", "");
    }

    private MalformedSqlException(String message, String sql) {
        super(message);
        this.sql = sql;
    }

    public MalformedSqlException(String sql, JSQLParserException e) {
        super("Malformed SQL:\n" + sql, e);
        this.sql = sql;
    }
}
