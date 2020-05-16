package br.com.cwi.technicalchallenge.controller.request;

import br.com.cwi.technicalchallenge.domain.VoteOption;

public class VoteRequest {


    private long id;
    private VoteOption voteOption;

    public long getId() {
        return id;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }
}
