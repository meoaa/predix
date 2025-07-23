package project.predix.store.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.predix.common.ApiResponse;
import project.predix.member.domain.Member;
import project.predix.store.dto.CreateRequestDto;
import project.predix.store.dto.CreateResponseDto;
import project.predix.store.service.StoreService;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/store")
public class StoreApiController {

    private final StoreService storeService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateResponseDto>> createStore
            (@RequestBody CreateRequestDto requestDto,
             @AuthenticationPrincipal Member authenticatedMember ){
        log.info("authenticatedMember  : {} ", authenticatedMember );

        CreateResponseDto resDto = storeService.saveStore(requestDto, authenticatedMember);
        return ResponseEntity.ok(ApiResponse.of(200, "가게 등록이 성공적으로 완료되었습니다.", resDto));
    }
}

