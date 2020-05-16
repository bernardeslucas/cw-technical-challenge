package br.com.cwi.technicalchallenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VotingSessionClosedException extends RuntimeException {

    public VotingSessionClosedException(String message) {
        super(String.format("The voting session for topic id '%s' is closed.", message));
    }
}