package nh.logTrace.save.db.repository;

import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.db.DbAdapter;
import nh.logTrace.save.db.sql.jdbc.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcLogRepository implements LogRepository {

    private final Logger logger = LoggerFactory.getLogger(JdbcLogRepository.class);
    private final DataSource dataSource;
    private final Sql sql;

    public JdbcLogRepository(DataSource dataSource, DbAdapter dbAdapter) {
        this.dataSource = dataSource;
        this.sql = dbAdapter.getSql();
    }

    @Override
    public void initTable() {
        executeQuery(sql.getCreateTableSql(), null);
    }

    @Override
    public List<LogEntity> findAll() {
        return executeQueryForAllList(sql.getSelectAllSql());
    }

    @Override
    public Long save(LogEntity logEntity) {
        // not supported type
        executeQuery(sql.getInsertSql(), logEntity);
        return logEntity.getId();
    }

    @Override
    public LogEntity findById(Long id) {
        return executeQueryForObject(sql.getSelectSql(), id);
    }

    private void executeQuery(String sql, LogEntity entity) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            if (entity != null) {
                pstmt.setString(1, entity.getTransactionId());
                pstmt.setString(2, entity.getClassName());
                pstmt.setString(3, entity.getMethodName());
                pstmt.setObject(4, entity.getArgs());
                pstmt.setObject(5, entity.getResult());
                pstmt.setString(6, entity.getThrowableMessage());
                pstmt.setObject(7, entity.getThrowableStackTrace());
                pstmt.setObject(8, entity.getCreatedAt());
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private LogEntity executeQueryForObject(String sql, Long id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            if (id != null) {
                pstmt.setLong(1, id);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                LogEntity logEntity = new LogEntity();
                logEntity.setId(rs.getLong(1));
                logEntity.setTransactionId(rs.getString(2));
                logEntity.setClassName(rs.getString(3));
                logEntity.setMethodName(rs.getString(4));
                logEntity.setArgs((Object[]) rs.getObject(5));
                logEntity.setResult(rs.getObject(6));
                logEntity.setThrowableMessage(rs.getString(7));
                logEntity.setThrowableStackTrace((Throwable) rs.getObject(8));
                logEntity.setCreatedAt((LocalDateTime) rs.getObject(9));
                return logEntity;
            }

            return null;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<LogEntity> executeQueryForAllList(String sql) {
        List<LogEntity> logEntities = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                LogEntity logEntity = new LogEntity();
                logEntity.setId(rs.getLong(1));
                logEntity.setTransactionId(rs.getString(2));
                logEntity.setClassName(rs.getString(3));
                logEntity.setMethodName(rs.getString(4));
                logEntity.setArgs((Object[]) rs.getObject(5));
                logEntity.setResult(rs.getObject(6));
                logEntity.setThrowableMessage(rs.getString(7));
                logEntity.setThrowableStackTrace((Throwable) rs.getObject(8));
                logEntity.setCreatedAt((LocalDateTime) rs.getObject(9));
                logEntities.add(logEntity);
            }

            return logEntities;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
