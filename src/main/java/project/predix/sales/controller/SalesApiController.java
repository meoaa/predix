package project.predix.sales.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.predix.member.domain.Member;
import project.predix.sales.dto.SalesCreateRequestDto;
import project.predix.sales.service.SalesService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/sales")
@AllArgsConstructor
public class SalesApiController {

    private final SalesService salesService;

    @PostMapping("/create")
    public ResponseEntity<?> createSales(
            @RequestBody List<SalesCreateRequestDto> requestDtos,
            @AuthenticationPrincipal Member member){

        List<SalesCreateRequestDto> salesCreateRequestDtos = salesService.salesDataSortedAndSave(requestDtos, member);

        return ResponseEntity.ok("ok");
    }
}
