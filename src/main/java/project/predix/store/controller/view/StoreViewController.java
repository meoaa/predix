package project.predix.store.controller.view;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.predix.member.domain.Member;
import project.predix.store.dto.FindByMemberResponseDto;
import project.predix.store.service.StoreService;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/store")
public class StoreViewController {

    private final StoreService storeService;

    @GetMapping()
    public String storePage(@AuthenticationPrincipal Member member, Model model){
        return storeService.searchOptionalStoreByMember(member)
                .map(store -> {
                    model.addAttribute("store", store);
                    return "store/detail";
                })
                .orElseGet(()-> "/store/add");
    }

    @GetMapping("/edit")
    public String storeEditPage(@AuthenticationPrincipal Member member, Model model){
        FindByMemberResponseDto responseDto = storeService.searchStoreByMember(member);
        model.addAttribute("store", responseDto);

        return "/store/edit";
    }
}
