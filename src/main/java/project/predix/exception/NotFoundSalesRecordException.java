package project.predix.exception;

public class NotFoundSalesRecordException extends RuntimeException{
    public NotFoundSalesRecordException() {
        super("일치하는 매출값이 존재하지 않습니다.");
    }

    public NotFoundSalesRecordException(String message) {
        super(message);
    }

    public NotFoundSalesRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundSalesRecordException(Throwable cause) {
        super(cause);
    }

    protected NotFoundSalesRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
