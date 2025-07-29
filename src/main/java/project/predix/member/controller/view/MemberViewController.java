package project.predix.member.controller.view;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.predix.member.domain.Member;
import project.predix.member.dto.ProfileResponseDto;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/profile")
public class MemberViewController {

    @GetMapping("")
    public String viewMyProfile(
            @AuthenticationPrincipal Member authenticatedMember,
            Model model){
        ProfileResponseDto responseDto = ProfileResponseDto.from(authenticatedMember);
        model.addAttribute("member", responseDto);
        return "/profile/profile";
    }

    @GetMapping("/edit")
    public String editProfile(
            @AuthenticationPrincipal Member authenticatedMember,
            Model model){

        ProfileResponseDto responseDto = ProfileResponseDto.from(authenticatedMember);
        model.addAttribute("member", responseDto);
        return "/profile/edit";
    }

    @GetMapping("/password")
    public String changePassword(){

        return "/profile/password";
    }
}
