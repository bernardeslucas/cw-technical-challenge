package br.com.cwi.technicalchallenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PautaNotFoundException extends RuntimeException {

    public PautaNotFoundException(String message) {
        super(String.format("Pauta com id '%s' não encontrada.", message));
    }

}
