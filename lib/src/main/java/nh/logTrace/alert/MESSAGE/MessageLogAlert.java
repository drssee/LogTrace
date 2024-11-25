package nh.logTrace.alert.MESSAGE;

import nh.logTrace.alert.LogAlert;
import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;

public class MessageLogAlert implements LogAlert {

    @Override
    public void alert(ThreadStatus threadStatus, LogDto logDto) {
        // TODO message 기능 추가 필요
        System.out.println("call message alert");
    }
}
