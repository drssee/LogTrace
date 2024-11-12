package nh.logTrace.alert;

import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;

public interface LogAlert {

    void alert(ThreadStatus threadStatus, LogDto logDto);
}
