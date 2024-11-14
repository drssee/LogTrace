package nh.logTrace.save.db.repository;

import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.db.DbAdapter;

import java.util.List;

public class JpaLogRepository implements LogRepository {

    private final DbAdapter dbAdapter;

    public JpaLogRepository(DbAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    @Override
    public void initTable() {

    }

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
