package project.predix.weather.sync;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.predix.weather.event.StoreRegisteredEvent;
import project.predix.weather.repistory.WeatherDailyRepository;

import java.time.LocalDate;


/**
 * 매장 등록 이벤트 수신 후 날씨 데이터 동기화.
 * <p>
 * 1) 최근 90일 데이터를 **즉시** 적재하여 예측 기능이 바로 동작하도록 한다.
 * 2) 같은 스레드에서 90일 적재가 끝나면, 별도 TaskExecutor 스레드에서 5년 백필을 이어서 수행한다.
 *    (syncRange 내부에서 ThreadPoolTaskExecutor 사용)
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherSyncListener {

    private final WeatherSyncService syncService;
    private final WeatherDailyRepository repository;
    /**
     * StoreRegisteredEvent → 비동기 실행 (@Async)
     * ThreadPoolTaskExecutor bean name: "weatherTaskExecutor" (see AsyncConfig)
     */

    @Async("weatherTaskExecutor")
    @EventListener
    public void handle(StoreRegisteredEvent event){
        int stationId = event.getStationCode();
        log.info("[WeatherSyncListener] received event for station {}", stationId);

        // 이미 어느 정도 데이터가 있으면 빠른 부트스트랩은 생략
        if (repository.existsByStationId(stationId)) {
            log.info("Weather data already present for station {} – skip bootstrap", stationId);
            return;
        }

        // 1) 최근 90일 동기 수집 (네트워크 호출이지만 상대적으로 짧음)
        syncService.syncRecent(stationId, 90);
        log.info("[WeatherSyncListener] station {} bootstrap(90d) completed", stationId);

        // 2) 5년 백필 – TaskExecutor 내부 스레드에서 장시간 수행
        syncService.syncRange(stationId, LocalDate.now().minusYears(5), LocalDate.now().minusDays(1));
        log.info("[WeatherSyncListener] station {} backfill(5y) job submitted", stationId);
    }
}
