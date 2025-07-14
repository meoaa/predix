package proejct.predix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/")
    public String mainPage(){
        log.info("main page");
        boolean login = true;
        if(login){
            return "index";
        }
        return "login";
    }
}
