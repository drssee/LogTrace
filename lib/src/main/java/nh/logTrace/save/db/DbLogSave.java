package nh.logTrace.save.db;

import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.XmlLoggerInitializer;
import nh.logTrace.save.db.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (logRepository - jdbc/jpa/mybatis logRepository) -> (dbAdapter) -> (sql - oracle/maira/h2 sql)
 */
public class DbLogSave implements LogSave {

    private static final Logger logger = LoggerFactory.getLogger(DbLogSave.class);
    private final String DEFAULT_CONFIG_FILE = "logback-logtrace-console.xml";
    private final LogRepository logRepository;

    public DbLogSave(LogRepository logRepository) {
        XmlLoggerInitializer.init(DEFAULT_CONFIG_FILE);
        this.logRepository = logRepository;
        this.logRepository.initTable();
    }

    @Override
    public Long save(LogDto logDto) {
        logger.info("id {},  log db save", logDto.getTransactionId());

        LogEntity logEntity = new LogEntity();
        logEntity.setId(logDto.getId());
        logEntity.setTransactionId(logDto.getTransactionId());
        logEntity.setClassName(logDto.getClassName());
        logEntity.setMethodName(logDto.getMethodName());
        logEntity.setArgs(logDto.getArgs());
        logEntity.setResult(logDto.getResult());
        logEntity.setThrowableMessage(logDto.getThrowableMessage());
        logEntity.setCreatedAt(logDto.getCreatedAt());

        return this.logRepository.save(logEntity);
    }
}
