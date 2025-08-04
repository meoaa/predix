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

@Controller
@AllArgsConstructor
@Slf4j
public class MainController {

    private final MemberService memberService;

    @GetMapping("/")
    public String mainPage(Authentication auth){
        return "index";
    }

    @GetMapping("/sales")
    public String salesForm(@AuthenticationPrincipal Member member) {
        MemberResponseDto foundMember = memberService.findMemberById(member.getId());
        System.out.println("foundMember = " + foundMember);

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
