package project.predix.common;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int code;
    private String message;
    private boolean success;

    private ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.success = false;
    }

    public static ErrorResponse of(int code, String message){
        return new ErrorResponse(code, message);
    }
}
