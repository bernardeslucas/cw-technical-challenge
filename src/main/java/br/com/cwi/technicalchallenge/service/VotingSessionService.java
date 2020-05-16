package br.com.cwi.technicalchallenge.service;

import br.com.cwi.technicalchallenge.controller.request.VotingSessionRequest;
import br.com.cwi.technicalchallenge.domain.Pauta;
import br.com.cwi.technicalchallenge.domain.VoteOption;
import br.com.cwi.technicalchallenge.domain.VotingSession;
import br.com.cwi.technicalchallenge.repository.PautaRepository;
import br.com.cwi.technicalchallenge.repository.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotingSessionService {

    @Autowired
    private VotingSessionRepository votingSessionRepository;

    @Autowired
    private PautaRepository pautaRepository;





}
