package br.com.cwi.technicalchallenge.exceptions;

import org.springframework.http.HttpStatus;

public enum VotingSessionExceptionEnum {
    VOTING_SESSION_REQUEST_INVALID("Voting Session request invalid or empty.", HttpStatus.BAD_REQUEST),
    TOPIC_NOT_FOUND("Topic not found", HttpStatus.NOT_FOUND),
    VOTING_SESSION_ALREADY_DEFINED("This topic already has a voting session defined", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    VotingSessionExceptionEnum(String message, HttpStatus httpStatus) {
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
