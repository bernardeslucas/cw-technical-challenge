package br.com.cwi.technicalchallenge.controller;

import br.com.cwi.technicalchallenge.controller.request.VoteRequest;
import br.com.cwi.technicalchallenge.controller.request.VotingSessionRequest;
import br.com.cwi.technicalchallenge.controller.response.PautaResponse;
import br.com.cwi.technicalchallenge.domain.Pauta;
import br.com.cwi.technicalchallenge.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/pautas"})
public class PautaController {

    @Autowired
    private PautaService pautaService;

    //pautas
    @RequestMapping(method = RequestMethod.GET)
    private List<PautaResponse> findAllPautas() {
        return pautaService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private PautaResponse findById(@PathVariable("id") long id) {
        return pautaService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    private void create(@RequestBody Pauta pauta) {
        pautaService.create(pauta);
    }

    //edit pauta

    //start voting session
    @PostMapping("{idPauta}/start")
    private void startVotingSession(@PathVariable("idPauta") long idPauta, @RequestBody VotingSessionRequest votingSessionRequest) {
        pautaService.start(idPauta, votingSessionRequest);
    }

    //vote
    @PostMapping("{idPauta}/vote")
    private void votar(@PathVariable("idPauta") long id, @RequestBody VoteRequest voteRequest) {
        pautaService.createVote(id, voteRequest);

    }
}
