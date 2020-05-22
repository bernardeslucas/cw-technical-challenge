package br.com.cwi.technicalchallenge.controller.v1;

import br.com.cwi.technicalchallenge.config.SwaggerConfig;
import br.com.cwi.technicalchallenge.controller.v1.request.TopicRequest;
import br.com.cwi.technicalchallenge.controller.v1.request.VoteRequest;
import br.com.cwi.technicalchallenge.controller.v1.request.VotingSessionRequest;
import br.com.cwi.technicalchallenge.controller.v1.response.TopicResponse;
import br.com.cwi.technicalchallenge.domain.Topic;
import br.com.cwi.technicalchallenge.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({"v1/topics"})
@Api(tags = {SwaggerConfig.TAG_1})
public class TopicController {

    @Autowired
    private final TopicService topicService;

    //topics
    @ApiOperation(value = "Gets all topics", response = TopicResponse[].class, produces = "application/json")
    @GetMapping
    public ResponseEntity<List<TopicResponse>> findAllTopics() {
        log.info("Searching all topics...");
        return ResponseEntity.ok(topicService.findAll());
    }

    @ApiOperation(value = "Gets a specific topic", response = TopicResponse.class, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Topic not found for the given id.")})
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") long id) {
        log.info("Searching a specific topic...");
        TopicResponse topicResponse;

        try {
            topicResponse = topicService.findById(id);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        log.info("Found it!");
        return ResponseEntity.ok(topicResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Register a new topic", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request.")})
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody TopicRequest topicRequest) {
        log.info("Creating topic...");
        Topic topic;
        try {
            topic = topicService.save(topicRequest);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        log.info("Topic successfully created!");
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }

    //start voting session
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Start a voting session in a specific topic", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Topic not found for the given id.")})
    @PostMapping("{idTopic}/start")
    public ResponseEntity<Object> startVotingSession(@PathVariable("idTopic") long idTopic, @RequestBody VotingSessionRequest votingSessionRequest) {
        log.info("Starting voting session...");

        try {
            topicService.start(idTopic, votingSessionRequest);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        log.info("Voting Session started in the specific topic informed!");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //vote
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Vote in a specific topic", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Topic or associate not found for the given id.")})
    @PostMapping("{idTopic}/vote")
    public ResponseEntity<Object> vote(@PathVariable("idTopic") long id, @RequestBody VoteRequest voteRequest) {
        log.info("Registering vote...");

        try {
            topicService.createVote(id, voteRequest);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        log.info("Vote successfully registered!");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
