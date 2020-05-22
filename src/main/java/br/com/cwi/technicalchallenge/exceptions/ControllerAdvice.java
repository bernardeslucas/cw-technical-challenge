package br.com.cwi.technicalchallenge.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "There is a notReadableException in one of the attributes.")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleHttpMessageNotReadableException(Exception e) {
        log.error(e.getMessage(), e);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "There is a inputMismatchException in one of the attributes.")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void methodArgumentTypeMismatchException(Exception e) {
        log.error(e.getMessage(), e);
    }
}
