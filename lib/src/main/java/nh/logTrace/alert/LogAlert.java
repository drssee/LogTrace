package nh.logTrace.alert;

import nh.logTrace.common.aop.Log;
import nh.logTrace.common.aop.ThreadStatus;

public interface LogAlert {

    void alert(ThreadStatus threadStatus, Log log);
}
