package nh.logTrace.save.db.repository;

import nh.logTrace.common.domain.LogEntity;

import java.util.List;

public interface LogRepository {

    Long save(LogEntity logEntity);
    LogEntity findById(Long id);
    List<LogEntity> findAll();
}
