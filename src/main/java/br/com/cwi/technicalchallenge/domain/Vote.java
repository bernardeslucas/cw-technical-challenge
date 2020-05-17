package br.com.cwi.technicalchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "option")
    private VoteOption voteOption;

    @Column(name = "date")
    private LocalDateTime voteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associate_id")
    private Associate associate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "voting_session_id")
    private VotingSession votingSession;

    public Vote() {
    }

    public Vote(Long id, VoteOption voteOption, LocalDateTime voteDate, Associate associate, VotingSession votingSession) {
        this.id = id;
        this.voteOption = voteOption;
        this.voteDate = voteDate;
        this.associate = associate;
        this.votingSession = votingSession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }

    public LocalDateTime getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDateTime voteDate) {
        this.voteDate = voteDate;
    }

    public Associate getAssociate() {
        return associate;
    }

    public void setAssociate(Associate associate) {
        this.associate = associate;
    }

    public VotingSession getVotingSession() {
        return votingSession;
    }

    public void setVotingSession(VotingSession votingSession) {
        this.votingSession = votingSession;
    }
}
