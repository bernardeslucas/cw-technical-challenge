package br.com.cwi.technicalchallenge.domain;

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

    @OneToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "votingSession")
    @Column(name = "vote_id")
    private List<Vote> votes;

    public VotingSession() {
    }

    public VotingSession(LocalDateTime startDate, LocalDateTime endDate, Topic topic, List<Vote> votes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.topic = topic;
        this.votes = votes;
    }

    public boolean isClosed() {
        return this.getEndDate() == null || LocalDateTime.now().isAfter(this.getEndDate());
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "VotingSession{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", topic=" + topic +
                ", votes=" + votes +
                '}';
    }
}
