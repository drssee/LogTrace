package nh.logTrace.save.file;

import nh.logTrace.common.domain.LogDto;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.XmlLoggerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLogSave implements LogSave {

    private static final Logger logger = LoggerFactory.getLogger(FileLogSave.class);
    private final String DEFAULT_CONFIG_FILE = "logback-logtrace-file.xml";

    public FileLogSave() {
        XmlLoggerInitializer.init(DEFAULT_CONFIG_FILE);
    }

    @Override
    public void save(LogDto logDto) {
        // 예외 로그 호출
        boolean isThrowable = logDto.getThrowableStackTrace() != null;
        if (isThrowable) {
            logger.error("{}[TransactionId: {}] Exception in {}.{}() with arguments: {}, cause: {}",
                    logDto.getPrefix(), logDto.getTransactionId(), logDto.getClassName(), logDto.getMethodName(), logDto.getArgs(), logDto.getThrowableMessage(), logDto.getThrowableStackTrace());
            return;
        }

        // 정상 로그 호출
        boolean isEnteringLog = logDto.getResult() == null;

        if (isEnteringLog) {
            // 메서드 호출 시작 로그
            logger.info("{}[TransactionId: {}] Entering {}.{}() with arguments: {}",
                    logDto.getPrefix(), logDto.getTransactionId(), logDto.getClassName(), logDto.getMethodName(), logDto.getArgs());
        } else {
            // 메서드 호출 종료 로그
            logger.info("{}[TransactionId: {}] Exiting {}.{}() with result: {}",
                    logDto.getPrefix(), logDto.getTransactionId(), logDto.getClassName(), logDto.getMethodName(), logDto.getResult());
        }
    }
}
