package nh.logTrace.save;

import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;

public interface LogSave {

    // TODO 어플리케이션 repository 계층에서 예외 발생시 실행 로그 쿼리 찍히도록 기능 추가
    // TODO 로그 설정시 라이브러리 기본 설정값(xml)이 아닌 커스텀 값을 사용할 수 있도록 기능 추가
    void save(ThreadStatus threadStatus, LogDto logDto);
}
