package br.com.cwi.technicalchallenge.repository;

import br.com.cwi.technicalchallenge.domain.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, Long> {

   Associate findById(long id);

}
