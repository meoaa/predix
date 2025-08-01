package project.predix.sales.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.predix.common.ApiResponse;
import project.predix.member.domain.Member;
import project.predix.sales.domain.Sales;
import project.predix.sales.domain.SalesType;
import project.predix.sales.dto.SalesChartDataDto;
import project.predix.sales.dto.SalesCreateRequestDto;
import project.predix.sales.dto.SalesResponseDto;
import project.predix.sales.service.SalesService;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/sales")
@AllArgsConstructor
public class SalesApiController {

    private final SalesService salesService;

    @PostMapping
    public ResponseEntity<ApiResponse<List<SalesCreateRequestDto>>> createSales(
            @RequestBody List<SalesCreateRequestDto> requestDtos,
            @AuthenticationPrincipal Member member){


        List<SalesCreateRequestDto> salesCreateRequestDtos = salesService.salesDataSortedAndSave(requestDtos, member);

        return ResponseEntity.ok(ApiResponse.of(200, "성공적으로 등록되었습니다.",salesCreateRequestDtos));
    }

    @GetMapping("/{type}")
    public ResponseEntity<ApiResponse<List<SalesResponseDto>>> getSalesData(
            @AuthenticationPrincipal Member member,
            @PathVariable String type){
        log.info("type :{}", type);
        SalesType salesType = type.equals("month") ? SalesType.MONTH : SalesType.WEEK;
        List<SalesResponseDto> salesDataDtos = salesService.getSalesDataDtos(member.getStore().getId(), salesType);

        return ResponseEntity.ok(ApiResponse.of(200, "데이터 로딩 완료", salesDataDtos));

    }

    @GetMapping("/chart")
    public ResponseEntity<ApiResponse<Map<SalesType, SalesChartDataDto>>> getSalesChartData(@AuthenticationPrincipal Member authenticatedMember){
        Map<SalesType, SalesChartDataDto> salesData = salesService.getSalesDataForChart(authenticatedMember.getStore().getId());

        log.info("salesData : {}", salesData);
        return ResponseEntity.ok(ApiResponse.of(200, "데이터를 성공적으로 불러왔습니다.", salesData));
    }

}
