package br.com.cwi.technicalchallenge.service;

import br.com.cwi.technicalchallenge.controller.request.TopicRequest;
import br.com.cwi.technicalchallenge.controller.request.VoteRequest;
import br.com.cwi.technicalchallenge.controller.request.VotingSessionRequest;
import br.com.cwi.technicalchallenge.controller.response.TopicResponse;
import br.com.cwi.technicalchallenge.domain.*;
import br.com.cwi.technicalchallenge.exceptions.TopicExceptionEnum;
import br.com.cwi.technicalchallenge.exceptions.VoteExceptionEnum;
import br.com.cwi.technicalchallenge.exceptions.VotingSessionExceptionEnum;
import br.com.cwi.technicalchallenge.repository.AssociateRepository;
import br.com.cwi.technicalchallenge.repository.TopicRepository;
import br.com.cwi.technicalchallenge.repository.VoteRepository;
import br.com.cwi.technicalchallenge.repository.VotingSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicService {

    private final ObjectMapper objectMapper;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private AssociateRepository associateRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;
    @Autowired
    private VoteRepository voteRepository;

    public TopicService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //topic
    private boolean isTopicRequestValid(Topic topic) {

        return topic != null &&
                topic.getTitle() != null &&
                topic.getDescription() != null &&
                !topic.getDescription().isEmpty() &&
                !topic.getTitle().isEmpty();
    }

    public void create(TopicRequest topicRequest) {

        Topic topic = objectMapper.convertValue(topicRequest, Topic.class);

        if (isTopicRequestValid(topic)) {

            topicRepository.save(topic);

        } else {
            throw new ResponseStatusException(TopicExceptionEnum.TOPIC_REQUEST_INVALID.getHttpStatus(),
                    TopicExceptionEnum.TOPIC_REQUEST_INVALID.getMessage());
        }
    }

    private TopicResponse getTopicResponse(Topic topic) {

        TopicResponse topicResponse = objectMapper.convertValue(topic, TopicResponse.class);

        VotingSession votingSession = votingSessionRepository.findByTopic(topic);

        if (votingSession != null) {
            if (votingSession.isClosed()) {
                topicResponse.setResult(String.format(result(topic), "Closed"));
            } else {
                topicResponse.setResult(String.format(result(topic), "Running"));
            }
        } else {
            topicResponse.setResult("Voting session didn't start yet.");
        }
        return topicResponse;
    }

    public List<TopicResponse> findAll() {

        return topicRepository.findAll().stream()
                .map(this::getTopicResponse)
                .collect(Collectors.toList());
    }

    public TopicResponse findById(long idTopic) {
        Topic topic = topicRepository.findById(idTopic);

        if (topic == null) {
            //throw new
            throw new ResponseStatusException(TopicExceptionEnum.TOPIC_NOT_FOUND.getHttpStatus(),
                    TopicExceptionEnum.TOPIC_NOT_FOUND.getMessage());
        }

        return getTopicResponse(topic);
    }

    //voting session
    private boolean isVotingSessionRequestValid(VotingSessionRequest votingSessionRequest) {
        return votingSessionRequest.getDurationMinutes() > 0;
    }

    public void start(long idTopic, VotingSessionRequest votingSessionRequest) {
        if (isVotingSessionRequestValid(votingSessionRequest)) {
            Topic topic = topicRepository.findById(idTopic);

            if (topic == null) {
                throw new ResponseStatusException(VotingSessionExceptionEnum.TOPIC_NOT_FOUND.getHttpStatus(),
                        VotingSessionExceptionEnum.TOPIC_NOT_FOUND.getMessage());
            }

            if (votingSessionRepository.findByTopic(topic) != null) {
                throw new ResponseStatusException(VotingSessionExceptionEnum.VOTING_SESSION_ALREADY_DEFINED.getHttpStatus(),
                        VotingSessionExceptionEnum.VOTING_SESSION_ALREADY_DEFINED.getMessage());
            }

            VotingSession votingSession = new VotingSession();

            votingSession.setStartDate(LocalDateTime.now());
            votingSession.setTopic(topic);

            //setting voting session's duration
            if (votingSessionRequest.getDurationMinutes() == null) {
                votingSession.setEndDate(LocalDateTime.now().plusMinutes(1));
            } else {
                votingSession.setEndDate(LocalDateTime.now().plusMinutes(votingSessionRequest.getDurationMinutes()));
            }
            votingSessionRepository.save(votingSession);
        } else {
            throw new ResponseStatusException(VotingSessionExceptionEnum.VOTING_SESSION_REQUEST_INVALID.getHttpStatus(),
                    VotingSessionExceptionEnum.VOTING_SESSION_REQUEST_INVALID.getMessage());
        }
    }

    //vote
    private boolean isVoteRequestValid(VoteRequest voteRequest) {
        return voteRequest.getIdAssociate() >= 1 &&

                voteRequest.getVoteOption() != null;
    }

    private boolean alreadyVoted(Associate associate, VotingSession votingSession) {
        return votingSession.getVotes()
                .stream()
                .anyMatch(vs -> vs.getAssociate().getId().equals(associate.getId()));
    }

    public void createVote(long idTopic, VoteRequest voteRequest) {

        if (isVoteRequestValid(voteRequest)) {

            Topic topic = topicRepository.findById(idTopic);
            VotingSession votingSession = votingSessionRepository.findByTopic(topic);

            if (votingSession.isClosed()) {
                throw new ResponseStatusException(VoteExceptionEnum.VOTING_SESSION_CLOSED.getHttpStatus(),
                        VoteExceptionEnum.VOTING_SESSION_CLOSED.getMessage());
            }

            Associate associate = associateRepository.findById(voteRequest.getIdAssociate());
            System.out.println(voteRequest.getIdAssociate());

            if (associate == null) {
                throw new ResponseStatusException(VoteExceptionEnum.ASSOCIATE_NOT_FOUND.getHttpStatus(),
                        VoteExceptionEnum.ASSOCIATE_NOT_FOUND.getMessage());
            }
            //implement CPF check

            if (alreadyVoted(associate, votingSession)) {
                throw new ResponseStatusException(VoteExceptionEnum.ALREADY_VOTED.getHttpStatus(),
                        VoteExceptionEnum.ALREADY_VOTED.getMessage());
            }

            Vote vote = new Vote();

            vote.setVoteDate(LocalDateTime.now());
            vote.setAssociate(associate);
            vote.setVoteOption(voteRequest.getVoteOption());
            vote.setVotingSession(votingSession);

            votingSession.addVote(vote);

            votingSessionRepository.save(votingSession);
            voteRepository.save(vote);
        } else {
            throw new ResponseStatusException(VoteExceptionEnum.VOTE_REQUEST_INVALID.getHttpStatus(),
                    VoteExceptionEnum.VOTE_REQUEST_INVALID.getMessage());
        }
    }

    // result
    private String result(Topic topic) {

        VotingSession votingSession = votingSessionRepository.findByTopic(topic);

        long countYesVotes = votingSession.getVotes()
                .stream()
                .filter(vs -> vs.getVoteOption() == VoteOption.YES)
                .count();

        long countNoVotes = votingSession.getVotes().size() - countYesVotes;

        return "%s votation -> YES votes: [" + countYesVotes + "], NO votes: [" + countNoVotes + "].";
    }

}
