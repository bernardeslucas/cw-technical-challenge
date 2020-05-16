package br.com.cwi.technicalchallenge.service;

import br.com.cwi.technicalchallenge.controller.request.VoteRequest;
import br.com.cwi.technicalchallenge.controller.request.VotingSessionRequest;
import br.com.cwi.technicalchallenge.controller.response.PautaResponse;
import br.com.cwi.technicalchallenge.domain.*;
import br.com.cwi.technicalchallenge.exceptions.PautaNotFoundException;
import br.com.cwi.technicalchallenge.exceptions.VotingSessionClosedException;
import br.com.cwi.technicalchallenge.repository.AssociateRepository;
import br.com.cwi.technicalchallenge.repository.PautaRepository;
import br.com.cwi.technicalchallenge.repository.VoteRepository;
import br.com.cwi.technicalchallenge.repository.VotingSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class PautaService {

    private final ObjectMapper objectMapper;
    @Autowired
    private PautaRepository pautaRepository;
    @Autowired
    private AssociateRepository associateRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;
    @Autowired
    private VoteRepository voteRepository;

    public PautaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //pauta
    public void create(Pauta pauta) {
        pautaRepository.save(pauta);
    }

    private PautaResponse getPautaResponse(Pauta pauta) {

        PautaResponse pautaResponse = objectMapper.convertValue(pauta, PautaResponse.class);

        VotingSession votingSession = votingSessionRepository.findByPauta(pauta);

        pautaResponse.setClosed(checkVotingSessionIsClosed(votingSession));

        if (pautaResponse.isClosed()) {
            pautaResponse.setResult(String.format(result(pauta), "encerrada"));
        } else {
            pautaResponse.setResult(String.format(result(pauta), "parcial"));
        }
        return pautaResponse;
    }

    public List<PautaResponse> findAll() {

        return pautaRepository.findAll().stream()
                .map(this::getPautaResponse)
                .collect(Collectors.toList());
    }

    public PautaResponse findById(long idPauta) {
        Pauta pauta = pautaRepository.findById(idPauta);

        if (pauta == null) {
            throw new PautaNotFoundException(String.valueOf(idPauta));
        }

        PautaResponse pautaResponse = getPautaResponse(pauta);
        return pautaResponse;
    }

    //start voting session
    public void start(long idPauta, VotingSessionRequest votingSessionRequest) {

        Pauta pauta = pautaRepository.findById(idPauta);

        if (pauta == null) {
            //throw
        }

        VotingSession votingSession = new VotingSession();

        votingSession.setStartDate(LocalDateTime.now());
        votingSession.setPauta(pauta);


        if (votingSessionRequest.getDuration() == null) {
            votingSession.setEndDate(LocalDateTime.now().plusMinutes(1));
        } else {
            votingSession.setEndDate(LocalDateTime.now().plusMinutes(votingSessionRequest.getDuration()));
        }

        votingSessionRepository.save(votingSession);
    }

    //vote
    public boolean checkVotingSessionIsClosed(VotingSession votingSession) {
        return LocalDateTime.now().isAfter(votingSession.getEndDate());
    }

    public void createVote(long idPauta, VoteRequest voteRequest) {

        Pauta pauta = pautaRepository.findById(idPauta);
        VotingSession votingSession = votingSessionRepository.findByPauta(pauta);

        if (checkVotingSessionIsClosed(votingSession)) {
            //throw exception
            throw new VotingSessionClosedException(String.valueOf(idPauta));
        }

        Associate associate = associateRepository.findById(voteRequest.getId()).get();

        Vote vote = new Vote();

        vote.setVoteDate(LocalDateTime.now());
        vote.setAssociate(associate);
        vote.setVoteOption(voteRequest.getVoteOption());
        vote.setVotingSession(votingSession);

        votingSession.addVote(vote);

        votingSessionRepository.save(votingSession);
        voteRepository.save(vote);
    }

    //resultado
    public String result(Pauta pauta) {

        VotingSession votingSession = votingSessionRepository.findByPauta(pauta);

        long countYesVotes = votingSession.getVotes()
                .stream()
                .filter(v -> v.getVoteOption() == VoteOption.YES)
                .count();
        long countNoVotes = votingSession.getVotes().size() - countYesVotes;

        return "Votação %s, votos SIM: " + countYesVotes + ", votos NÃO: " + countNoVotes;

    }

}
