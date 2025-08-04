package project.predix.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.predix.member.domain.Member;
import project.predix.member.dto.MemberResponseDto;
import project.predix.member.service.MemberService;
import project.predix.sales.service.SalesService;

@Controller
@AllArgsConstructor
@Slf4j
public class MainController {

    private final MemberService memberService;
    private final SalesService salesService;

    @GetMapping("/")
    public String mainPage(Authentication auth){
        return "index";
    }

    @GetMapping("/sales")
    public String salesForm(@AuthenticationPrincipal Member member) {
         if(member.getStore() == null){
            return "redirect:/store";
        }
       return "sales_record/input";
    }

    @GetMapping("/chart")
    public String chartPage(@AuthenticationPrincipal Member member){

        if(member.getStore() == null){
            return "redirect:/store";
        }
        long count = salesService.countByStoreId(member.getStore().getId());
        if(count == 0){
            return "redirect:/sales";
        }

        return "chart/chart";
    }

    @GetMapping("/prediction")
    public String predictionPage(){
        return "predix/prediction";
    }

}
