package br.com.cwi.technicalchallenge.repository;

import br.com.cwi.technicalchallenge.domain.Topic;
import br.com.cwi.technicalchallenge.domain.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {

    public VotingSession findByTopic(Topic topic);
}
