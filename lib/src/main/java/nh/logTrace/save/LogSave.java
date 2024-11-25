package nh.logTrace.save;

import nh.logTrace.common.domain.LogDto;

public interface LogSave {

    Long save(LogDto logDto);
}
