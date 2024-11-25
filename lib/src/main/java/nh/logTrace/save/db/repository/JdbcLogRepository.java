package nh.logTrace.save.db.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.db.DbAdapter;
import nh.logTrace.save.db.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcLogRepository implements LogRepository {

    private final Logger logger = LoggerFactory.getLogger(JdbcLogRepository.class);
    private final DataSource dataSource;
    private final Sql sql;
    private final ObjectMapper objectMapper;

    public JdbcLogRepository(DataSource dataSource, DbAdapter dbAdapter) {
        this.dataSource = dataSource;
        this.sql = dbAdapter.getSql();
        this.objectMapper = new ObjectMapper();
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
        return executeQuery(sql.getInsertSql(), logEntity);
    }

    @Override
    public LogEntity findById(Long id) {
        return executeQueryForObject(sql.getSelectSql(), id);
    }

    private Long executeQuery(String sql, LogEntity entity) {
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
                logEntity.setArgs(parse(rs.getString(5), Object[].class));
                logEntity.setResult(rs.getObject(6));
                logEntity.setThrowableMessage(rs.getString(7));
                logEntity.setCreatedAt(rs.getTimestamp(8).toLocalDateTime());
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
                logEntity.setArgs(parse(rs.getString(5), Object[].class));
                logEntity.setResult(rs.getObject(6));
                logEntity.setThrowableMessage(rs.getString(7));
                logEntity.setCreatedAt(rs.getTimestamp(8).toLocalDateTime());
                logEntities.add(logEntity);
            }

            return logEntities;

        } catch (SQLException e) {
            logger.error(e.getMessage());
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
