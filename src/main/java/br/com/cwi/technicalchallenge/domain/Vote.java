package br.com.cwi.technicalchallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "associate_id")
    private Associate associate;

    @ManyToOne
    @JoinColumn(name = "voting_session_id")
    private VotingSession votingSession;

}
