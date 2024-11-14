package nh.logTrace.save.db;

import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.XmlLoggerInitializer;
import nh.logTrace.save.db.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        // TODO repository->adapter->sql 가져와서, 로그저장 테이블 없으면 생성하기
        this.logRepository.initTable();
    }

    @Override
    public void save(ThreadStatus threadStatus, LogDto logDto) {
        // TODO 콘솔에 로그를 찍고, db에 저장
        logger.info("id {} save", threadStatus.getTransactionId());
    }
}
