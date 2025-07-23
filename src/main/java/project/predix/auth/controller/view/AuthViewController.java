package project.predix.auth.controller.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AuthViewController {

    @GetMapping("/login")
    public String loginPage(){
        return "/auth/login";
    }

    @GetMapping("/signup")
    public String signupPage(){
        return "/auth/signup";
    }


}
