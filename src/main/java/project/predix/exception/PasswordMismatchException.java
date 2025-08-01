package project.predix.exception;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException() {
        super("기존의 비밀번호가 일치하지 않습니다.");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordMismatchException(Throwable cause) {
        super(cause);
    }

    protected PasswordMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
