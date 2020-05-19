package br.com.cwi.technicalchallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
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


    public boolean isClosed() {
        return this.getEndDate() == null || LocalDateTime.now().isAfter(this.getEndDate());
    }

    public void addVote(Vote vote){
        votes.add(vote);
    }


}
