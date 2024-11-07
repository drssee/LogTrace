package nh.logTrace.save;

import nh.logTrace.common.aop.Log;
import nh.logTrace.common.aop.ThreadStatus;

public interface LogSave {

    void save(ThreadStatus threadStatus, Log log);
}
