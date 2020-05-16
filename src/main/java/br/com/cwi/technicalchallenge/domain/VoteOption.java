package br.com.cwi.technicalchallenge.domain;

public enum VoteOption {
    YES("Sim"),
    NO("Não");

    String description;

    VoteOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
