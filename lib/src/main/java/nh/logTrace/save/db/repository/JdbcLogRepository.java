package nh.logTrace.save.db.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.db.DbAdapter;
import nh.logTrace.save.db.sql.Sql;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcLogRepository {

    private final DataSource dataSource;
    private final Sql sql;
    private final ObjectMapper objectMapper;

    public JdbcLogRepository(DataSource dataSource, DbAdapter dbAdapter) {
        this.dataSource = dataSource;
        this.sql = dbAdapter.getSql();
        this.objectMapper = new ObjectMapper();
    }

    public void initTable() {
        executeQueryWithEntity(sql.getCreateTableSql(), null);
    }

    public List<LogEntity> findAll() {
        return executeQueryForList(sql.getSelectAllSql());
    }

    public Long save(LogEntity logEntity) {
        return executeQueryWithEntity(sql.getInsertSql(), logEntity);
    }

    public LogEntity findById(Long id) {
        return executeQueryForObject(sql.getSelectSql(), id);
    }

    public List<LogEntity> findLogsByCreatedAt(LocalDateTime start, LocalDateTime end) {
        return executeQueryForListWithLocalDateTime(sql.getSelectByCreatedAt(), start, end);
    }

    private Long executeQueryWithEntity(String sql, LogEntity entity) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if (entity != null) {
                pstmt.setString(1, entity.getTransactionId());
                pstmt.setString(2, entity.getClassName());
                pstmt.setString(3, entity.getMethodName());
                pstmt.setString(4, Arrays.toString(entity.getArgs()));
                pstmt.setString(5, entity.getResult() == null ? "" : entity.getResult().toString());
                pstmt.setString(6, entity.getThrowableMessage());
                pstmt.setObject(7, entity.getCreatedAt());
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }

            }

            pstmt.executeUpdate();
            return 0L;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private LogEntity executeQueryForObject(String sql, Long id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            if (id != null) {
                pstmt.setLong(1, id);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LogEntity logEntity = new LogEntity();
                    logEntity.setId(rs.getLong(1));
                    logEntity.setTransactionId(rs.getString(2));
                    logEntity.setClassName(rs.getString(3));
                    logEntity.setMethodName(rs.getString(4));
                    logEntity.setArgs(parse(rs.getString(5), Object[].class));
                    logEntity.setResult(rs.getObject(6));
                    logEntity.setThrowableMessage(rs.getString(7));
                    logEntity.setCreatedAt(rs.getTimestamp(8).toLocalDateTime());
                    return logEntity;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LogEntity> executeQueryForList(String sql) {
        List<LogEntity> logEntities = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    LogEntity logEntity = new LogEntity();
                    logEntity.setId(rs.getLong(1));
                    logEntity.setTransactionId(rs.getString(2));
                    logEntity.setClassName(rs.getString(3));
                    logEntity.setMethodName(rs.getString(4));
                    logEntity.setArgs(parse(rs.getString(5), Object[].class));
                    logEntity.setResult(rs.getObject(6));
                    logEntity.setThrowableMessage(rs.getString(7));
                    logEntity.setCreatedAt(rs.getTimestamp(8).toLocalDateTime());
                    logEntities.add(logEntity);
                }
            }

            return logEntities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LogEntity> executeQueryForListWithLocalDateTime(String sql, LocalDateTime... parameters) {
        List<LogEntity> logEntities = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i = 1; i <= parameters.length; i++) {
                pstmt.setTimestamp(i, Timestamp.valueOf(parameters[i - 1]));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LogEntity logEntity = new LogEntity();
                    logEntity.setId(rs.getLong(1));
                    logEntity.setTransactionId(rs.getString(2));
                    logEntity.setClassName(rs.getString(3));
                    logEntity.setMethodName(rs.getString(4));
                    logEntity.setArgs(parse(rs.getString(5), Object[].class));
                    logEntity.setResult(rs.getObject(6));
                    logEntity.setThrowableMessage(rs.getString(7));
                    logEntity.setCreatedAt(rs.getTimestamp(8).toLocalDateTime());
                    logEntities.add(logEntity);
                }
            }

            return logEntities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T parse(String json, Class<T> clazz) {
        T result;
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
        return result;
    }
}
