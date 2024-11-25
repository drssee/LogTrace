package nh.logTrace.save.db.sql;

public abstract class Sql {

    protected String createTableSql;
    protected String insertSql;
    protected String selectSql;
    protected String selectAllSql;

    public String getCreateTableSql() {
        return createTableSql;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public String getSelectAllSql() {
        return selectAllSql;
    }
}
