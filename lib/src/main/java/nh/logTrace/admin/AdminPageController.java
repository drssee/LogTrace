package nh.logTrace.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/log")
public class AdminPageController {

    @GetMapping("/index")
    public String test() {
        return "forward:/logTrace.html";
    }
}
