package project.predix.exception;

public class NotFoundStoreByMemberException extends RuntimeException{
    public NotFoundStoreByMemberException() {
        super("등록된 가게 정보가 없습니다.");
    }

    public NotFoundStoreByMemberException(String message) {
        super(message);
    }

    public NotFoundStoreByMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundStoreByMemberException(Throwable cause) {
        super(cause);
    }

    protected NotFoundStoreByMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
