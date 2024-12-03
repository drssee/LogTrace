package nh.logTrace.admin;

import nh.logTrace.common.domain.LogDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminPageService {

    private final AdminPageRepository adminPageRepository;

    public AdminPageService(AdminPageRepository adminPageRepository) {
        this.adminPageRepository = adminPageRepository;
    }

    // 현재 저장 방식과 관계없이 파일과 db 모두 날짜 기준으로 뒤져서 확인해야함
    public List<LogDto> findLogListByDateTime(LocalDateTime dateTime) {

        // 파일

        // DB

        return null;
    }
}
