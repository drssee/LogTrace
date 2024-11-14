package nh.logTrace.save.db.sql;

import javax.sql.DataSource;

public class MariaSql implements Sql {

    private final DataSource dataSource;

    public MariaSql(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
