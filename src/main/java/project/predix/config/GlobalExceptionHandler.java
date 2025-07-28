package project.predix.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.predix.common.ErrorResponse;
import project.predix.exception.DuplicateEmailException;
import project.predix.exception.DuplicateUsernameException;
import project.predix.exception.MemberNotFoundException;

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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(401).body(ErrorResponse.of(401, "아이디 또는 비밀번호가 틀렸습니다."));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException ex){
        return new ResponseEntity<>(ErrorResponse.of(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
