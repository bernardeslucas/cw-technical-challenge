package br.com.cwi.technicalchallenge.exceptions;

import org.springframework.http.HttpStatus;

public enum TopicExceptionEnum {
    TOPIC_NOT_FOUND("Topic not found.", HttpStatus.NOT_FOUND),
    TOPIC_REQUEST_INVALID("Topic request invalid or empty.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    TopicExceptionEnum(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
