package project.predix.weather.event;

import lombok.Getter;

@Getter
public class StoreRegisteredEvent {
    private final int stationCode;

    public StoreRegisteredEvent(int stationCode) {
        this.stationCode = stationCode;
    }

}
