package project.predix.exception;

public class DuplicateUsernameException extends RuntimeException{
    public DuplicateUsernameException() {
        super("이미 존재하는 ID 입니다.");
    }

    public DuplicateUsernameException(String message) {
        super("이미 존재하는 ID 입니다.: " + message);
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateUsernameException(Throwable cause) {
        super(cause);
    }

    protected DuplicateUsernameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
