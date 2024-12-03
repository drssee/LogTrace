package nh.logTrace.admin;

import nh.logTrace.common.domain.LogDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/log")
public class AdminPageController {

    private final AdminPageService adminPageService;

    public AdminPageController(AdminPageService adminPageService) {
        this.adminPageService = adminPageService;
    }

    @GetMapping("/index")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/logView")
    @ResponseBody
    public List<LogDto> logView(
            @RequestParam(name = "dateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        System.out.println("Received LocalDateTime: " + dateTime);

        return null;
    }
}
