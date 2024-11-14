package nh.logTrace.save.db;


import nh.logTrace.save.db.sql.H2Sql;
import nh.logTrace.save.db.sql.MariaSql;
import nh.logTrace.save.db.sql.OracleSql;
import nh.logTrace.save.db.sql.Sql;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * 어플리케이션에서 사용중인 DB 와 매칭시켜주는 어댑터 클래스
 * 1. 어플리케이션 라이브러리에 사용중인 DB드라이버 빈 조회 -> 없으면 예외
 * 2. 사용중인 DB와 매칭되는 sql 리턴
 */
public class DbAdapter {


    private List<String> DRIVERS = List.of("H2 JDBC Driver", "MariaDB Connector/J", "Oracle JDBC Driver");
    private Sql sql;
    private final DataSource dataSource;

    public DbAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
        initSql(dataSource);
    }

    private void initSql(DataSource dataSource) {
        try {
            String driverName = dataSource
                    .getConnection()
                    .getMetaData()
                    .getDriverName();

            if (DRIVERS.contains(driverName)) {

                switch (driverName) {
                    case "H2 JDBC Driver":
                        this.sql = new H2Sql(dataSource);
                        break;
                    case "MariaDB Connector/J":
                        this.sql = new MariaSql(dataSource);
                        break;
                    case "Oracle JDBC Driver":
                        this.sql = new OracleSql(dataSource);
                        break;
                }

            } else {
                throw new SQLException("db not supported");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Sql getSql() {
        return sql;
    }
}
