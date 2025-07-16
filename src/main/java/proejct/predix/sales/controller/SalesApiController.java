package proejct.predix.sales.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proejct.predix.sales.dto.SalesCreateRequestDto;
import proejct.predix.sales.service.SalesService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/sales")
@AllArgsConstructor
public class SalesApiController {

    private final SalesService salesService;

    @PostMapping("/create")
    public ResponseEntity<?> createSales(
            @RequestBody List<SalesCreateRequestDto> requestDtos){

        List<SalesCreateRequestDto> salesCreateRequestDtos = salesService.salesDataSortedAndSave(requestDtos);

        return ResponseEntity.ok("ok");
    }
}
