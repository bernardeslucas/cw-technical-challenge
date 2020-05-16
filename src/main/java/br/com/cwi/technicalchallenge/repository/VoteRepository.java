package br.com.cwi.technicalchallenge.repository;

import br.com.cwi.technicalchallenge.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
