package nh.logTrace.save.db.sql;

import javax.sql.DataSource;

public class H2Sql implements Sql{

    private final DataSource dataSource;

    public H2Sql(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
