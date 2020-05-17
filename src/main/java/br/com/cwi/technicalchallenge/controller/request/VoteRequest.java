package br.com.cwi.technicalchallenge.controller.request;

import br.com.cwi.technicalchallenge.domain.VoteOption;

public class VoteRequest {


    private long idAssociate;
    private VoteOption voteOption;

    public long getIdAssociate() {
        return idAssociate;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }
}
