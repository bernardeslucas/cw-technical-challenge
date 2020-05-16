package br.com.cwi.technicalchallenge.repository;

import br.com.cwi.technicalchallenge.domain.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    public Pauta findById(long id);
}
