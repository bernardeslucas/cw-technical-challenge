package br.com.cwi.technicalchallenge.exceptions;

import org.springframework.http.HttpStatus;

public enum VoteExceptionEnum {

    VOTE_REQUEST_INVALID("Vote request invalid or empty.", HttpStatus.BAD_REQUEST),
    ALREADY_VOTED("Associate already voted in this voting Session.", HttpStatus.BAD_REQUEST),
    VOTING_SESSION_CLOSED("This voting session is closed.", HttpStatus.BAD_REQUEST),
    ASSOCIATE_NOT_FOUND("Associate not found.", HttpStatus.BAD_REQUEST),
    CPF_NOT_AUTHORIZED("CPF not allowed to vote.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    VoteExceptionEnum(String message, HttpStatus httpStatus) {
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
