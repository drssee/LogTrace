package nh.logTrace.save.db.sql;

import javax.sql.DataSource;

public class OracleSql implements Sql {

    private final DataSource dataSource;

    public OracleSql(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
