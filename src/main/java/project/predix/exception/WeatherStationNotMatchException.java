package project.predix.exception;

public class WeatherStationNotMatchException extends RuntimeException{
    public WeatherStationNotMatchException() {
        super("기상 관측소 매핑 실패");
    }

    public WeatherStationNotMatchException(String message) {
        super("기상 관측소 매핑 실패 : " + message);
    }

    public WeatherStationNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherStationNotMatchException(Throwable cause) {
        super(cause);
    }

    protected WeatherStationNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
