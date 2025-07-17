package project.predix.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private boolean success;
    private T data;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.success = true;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(int code, String message,T data){
        return new ApiResponse<>(code, message, data);
    }
}
