package nh.logTrace.save.db;

import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.XmlLoggerInitializer;
import nh.logTrace.save.db.repository.JdbcLogRepository;

/**
 * (logRepository - jdbc/jpa/mybatis logRepository) -> (dbAdapter) -> (sql - oracle/maira/h2 sql)
 */
public class DbLogSave implements LogSave {

    private final String DEFAULT_CONFIG_FILE = "logback-logtrace-console.xml";
    private final JdbcLogRepository jdbcLogRepository;

    public DbLogSave(JdbcLogRepository jdbcLogRepository) {
        XmlLoggerInitializer.init(DEFAULT_CONFIG_FILE);
        this.jdbcLogRepository = jdbcLogRepository;
        this.jdbcLogRepository.initTable();
    }

    @Override
    public void save(LogDto logDto) {

        LogEntity logEntity = new LogEntity();
        logEntity.setId(logDto.getId());
        logEntity.setTransactionId(logDto.getTransactionId());
        logEntity.setClassName(logDto.getClassName());
        logEntity.setMethodName(logDto.getMethodName());
        logEntity.setArgs(logDto.getArgs());
        logEntity.setResult(logDto.getResult());
        logEntity.setThrowableMessage(logDto.getThrowableMessage());
        logEntity.setCreatedAt(logDto.getCreatedAt());

        this.jdbcLogRepository.save(logEntity);
    }
}
