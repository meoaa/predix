package project.predix.predictor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.predix.member.domain.Member;
import project.predix.predictor.dto.ForecastDto;
import project.predix.predictor.service.ForecastService;
import project.predix.sales.domain.SalesType;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forecast")
public class ForecastApiController {

    private final ForecastService forecastService;

    @GetMapping("/month")
    public List<ForecastDto> getMonth(
            @AuthenticationPrincipal Member authenticatedMember) throws IOException, InterruptedException {
        return forecastService.forecast(authenticatedMember.getStore().getId(), SalesType.MONTH);

    }


    @GetMapping("/week")
    public List<ForecastDto> getWeek(
            @AuthenticationPrincipal Member authenticatedMember) throws IOException, InterruptedException {
        return forecastService.forecast(authenticatedMember.getStore().getId(), SalesType.WEEK);

    }
}
