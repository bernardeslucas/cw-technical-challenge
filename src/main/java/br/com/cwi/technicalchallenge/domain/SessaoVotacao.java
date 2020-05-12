package br.com.cwi.technicalchallenge.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="sessao_votacao")
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;






}
