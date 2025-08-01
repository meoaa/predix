package project.predix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/")
    public String mainPage(Authentication auth){
        return "index";
    }

    @GetMapping("/sales")
    public String salesForm() {
       return "sales_record/input";
    }

    @GetMapping("/chart")
    public String chartPage(){
        return "chart/chart";
    }

    @GetMapping("/prediction")
    public String predictionPage(){
        return "predix/prediction";
    }

}
