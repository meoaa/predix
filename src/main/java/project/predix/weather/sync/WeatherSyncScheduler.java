package project.predix.weather.sync;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.predix.store.repository.StoreRepository;

@Component
@RequiredArgsConstructor
public class WeatherSyncScheduler {

    private final WeatherSyncService syncService;
    private final StoreRepository storeRepo;

    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul")
    public void dailyIncremental() {
        storeRepo.findDistinctStationIds()
                .forEach(id -> syncService.syncRecent(id, 1));
    }
}
