package br.com.cwi.technicalchallenge.controller;

import br.com.cwi.technicalchallenge.controller.request.VoteRequest;
import br.com.cwi.technicalchallenge.controller.request.VotingSessionRequest;
import br.com.cwi.technicalchallenge.controller.response.TopicResponse;
import br.com.cwi.technicalchallenge.domain.Topic;
import br.com.cwi.technicalchallenge.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"/topics"})
public class TopicController {

    @Autowired
    private TopicService topicService;

    //topics
    @RequestMapping(method = RequestMethod.GET)
    private List<TopicResponse> findAllTopics() {
        return topicService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private TopicResponse findById(@PathVariable("id") long id) {
        try {
            return topicService.findById(id);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    private void create(@RequestBody Topic topic) {
        try {
            topicService.create(topic);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    //start voting session
    @PostMapping("{idTopic}/start")
    private void startVotingSession(@PathVariable("idTopic") long idTopic, @RequestBody VotingSessionRequest votingSessionRequest) {
        try {
            topicService.start(idTopic, votingSessionRequest);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    //vote
    @PostMapping("{idTopic}/vote")
    private void vote(@PathVariable("idTopic") long id, @RequestBody VoteRequest voteRequest) {

        try {
            topicService.createVote(id, voteRequest);
        }
        catch (ResponseStatusException e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
