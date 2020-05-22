package br.com.cwi.technicalchallenge.repository;

import br.com.cwi.technicalchallenge.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findById(long id);

}
