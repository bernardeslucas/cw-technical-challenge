package br.com.cwi.technicalchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "voting_session")
public class VotingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private Long duration;

    @OneToOne
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @OneToMany(mappedBy = "votingSession")
    @Column(name = "vote_id")
    private List<Vote> votes;

    public VotingSession() {
    }

    public VotingSession(LocalDateTime startDate, Long duration, LocalDateTime endDate, Pauta pauta) {
        this.startDate = startDate;
        this.duration = duration;
        this.endDate = endDate;
        this.pauta = pauta;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    @JsonIgnore
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public void addVote(Vote vote){
        votes.add(vote);
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "VotingSession{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", duration=" + duration +
                ", endDate=" + endDate +
                ", pauta=" + pauta +
                ", votes=" + votes +
                '}';
    }
}
