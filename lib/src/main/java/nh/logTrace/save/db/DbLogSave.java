package nh.logTrace.save.db;

import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.XmlLoggerInitializer;
import nh.logTrace.save.db.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbLogSave implements LogSave {

    private static final Logger logger = LoggerFactory.getLogger(DbLogSave.class);
    private final String DEFAULT_CONFIG_FILE = "logback-logtrace-console.xml";
    private final LogRepository logRepository;

    public DbLogSave(LogRepository logRepository) {
        XmlLoggerInitializer.init(DEFAULT_CONFIG_FILE);
        this.logRepository = logRepository;
    }

    @Override
    public void save(ThreadStatus threadStatus, LogDto logDto) {
        // TODO 콘솔에 로그를 찍고, db에 저장
        logger.info("id {} save", threadStatus.getTransactionId());
    }
}
