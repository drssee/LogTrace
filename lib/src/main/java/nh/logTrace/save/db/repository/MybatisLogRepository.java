package nh.logTrace.save.db.repository;

import nh.logTrace.common.domain.LogEntity;

import java.util.List;

public class MybatisLogRepository implements LogRepository {

    @Override
    public List<LogEntity> findAll() {
        return List.of();
    }

    @Override
    public Long save(LogEntity logEntity) {
        return 0L;
    }

    @Override
    public LogEntity findById(Long id) {
        return null;
    }
}
