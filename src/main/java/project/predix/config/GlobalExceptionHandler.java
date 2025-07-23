package project.predix.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.predix.common.ErrorResponse;
import project.predix.exception.DuplicateEmailException;
import project.predix.exception.DuplicateUsernameException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(400,ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
