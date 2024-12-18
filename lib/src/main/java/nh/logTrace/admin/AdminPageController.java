package nh.logTrace.admin;

import nh.logTrace.common.config.ConfigProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/log")
public class AdminPageController {

    private final AdminPageService adminPageService;
    private final ConfigProperties configProperties;

    public AdminPageController(AdminPageService adminPageService, ConfigProperties configProperties) {
        this.adminPageService = adminPageService;
        this.configProperties = configProperties;
    }

    @GetMapping("/index")
    public String index() {
        return "forward:/index.html";
    }

    // TODO 관리자 기능 ip 체크 하는 어노테이션 추가 필요
    @GetMapping("/logView")
    @ResponseBody
    public List<String> logView(
            @RequestParam(name = "dateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        return adminPageService.findLogListByDateTime(dateTime);
    }

    @GetMapping("/errorView")
    @ResponseBody
    public List<String> errorView(
            @RequestParam(name = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return adminPageService.findErrLogListByDate(date);
    }

    @GetMapping("/config")
    @ResponseBody
    public ConfigProperties config() {
        return configProperties;
    }
}
