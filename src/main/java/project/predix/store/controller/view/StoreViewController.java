package project.predix.store.controller.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class StoreViewController {

    @GetMapping("/store")
    public String storePage(){
        return "store";
    }
}
