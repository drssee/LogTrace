package nh.logTrace.save.file;

import nh.logTrace.common.aop.Log;
import nh.logTrace.common.aop.ThreadStatus;
import nh.logTrace.save.LogSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLogSave implements LogSave {

    private static final Logger logger = LoggerFactory.getLogger(FileLogSave.class);

    public FileLogSave() {
        FileLoggerInitializer.init();
    }

    @Override
    public void save(ThreadStatus threadStatus, Log log) {
        // 예외 로그 호출
        boolean isThrowable = log.getThrowableStackTrace() != null;
        if (isThrowable) {
            logger.error("{}[TransactionId: {}] Exception in {}.{}() with arguments: {}, cause: {}",
                    log.getPrefix(), threadStatus.getTransactionId(), log.getClassName(), log.getMethodName(), log.getArgs(), log.getThrowableMessage(), log.getThrowableStackTrace());
            return;
        }

        // 정상 로그 호출
        boolean isEnteringLog = log.getResult() == null;

        if (isEnteringLog) {
            // 메서드 호출 시작 로그
            logger.info("{}[TransactionId: {}] Entering {}.{}() with arguments: {}",
                    log.getPrefix(), threadStatus.getTransactionId(), log.getClassName(), log.getMethodName(), log.getArgs());
        } else {
            // 메서드 호출 종료 로그
            logger.info("{}[TransactionId: {}] Exiting {}.{}() with result: {}",
                    log.getPrefix(), threadStatus.getTransactionId(), log.getClassName(), log.getMethodName(), log.getResult());
        }
    }
}
