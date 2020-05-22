package br.com.cwi.technicalchallenge.service;

import br.com.cwi.technicalchallenge.config.quartz.AutowiringSpringBeanJobFactory;
import br.com.cwi.technicalchallenge.config.quartz.QuartzJob;
import br.com.cwi.technicalchallenge.config.rabbit.RabbitConfig;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.quartz.DateBuilder.futureDate;

@Service
@AllArgsConstructor
@Slf4j
public class TopicService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    AssociateRepository associateRepository;
    @Autowired
    VotingSessionRepository votingSessionRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    ApplicationContext applicationContext;

    //topic
    private boolean isTopicRequestValid(Topic topic) {

        return topic != null &&
                topic.getTitle() != null &&
                topic.getDescription() != null &&
                !topic.getDescription().isEmpty() &&
                !topic.getTitle().isEmpty();
    }

    public Topic save(TopicRequest topicRequest) {

        Topic topic = objectMapper.convertValue(topicRequest, Topic.class);

        if (isTopicRequestValid(topic)) {

            topicRepository.save(topic);

        } else {
            throw new ResponseStatusException(TopicExceptionEnum.TOPIC_REQUEST_INVALID.getHttpStatus(),
                    TopicExceptionEnum.TOPIC_REQUEST_INVALID.getMessage());
        }
        return topic;
    }

    public TopicResponse getTopicResponse(Topic topic) {

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

    @Transactional
    public List<TopicResponse> findAll() {

        return topicRepository.findAll()
                .stream()
                .map(this::getTopicResponse)
                .collect(Collectors.toList())
                ;
    }

    @Transactional
    public TopicResponse findById(long idTopic) {
        Topic topic = topicRepository.findById(idTopic);

        if (topic == null) {
            //throw new
            throw new ResponseStatusException(TopicExceptionEnum.TOPIC_NOT_FOUND.getHttpStatus(),
                    TopicExceptionEnum.TOPIC_NOT_FOUND.getMessage());
//            throw new HttpClientErrorException(TopicExceptionEnum.TOPIC_NOT_FOUND.getHttpStatus(),
//                   TopicExceptionEnum.TOPIC_NOT_FOUND.getMessage());
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

            try {
                scheduleEndSession(idTopic, votingSessionRequest.getDurationMinutes());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            votingSessionRepository.save(votingSession);
        } else {
            throw new ResponseStatusException(VotingSessionExceptionEnum.VOTING_SESSION_REQUEST_INVALID.getHttpStatus(),
                    VotingSessionExceptionEnum.VOTING_SESSION_REQUEST_INVALID.getMessage());
        }
    }

    //vote and rules
    private void checkCpfStatus(String cpf) {

        String uri = "https://user-info.herokuapp.com/users/" + cpf;

        RestTemplate restTemplate = new RestTemplate();
        String result = "";
        try {
            result = restTemplate.getForObject(uri, String.class);
        } catch (HttpClientErrorException e){
            throw new ResponseStatusException(VoteExceptionEnum.CPF_INVALID.getHttpStatus(),
                    VoteExceptionEnum.CPF_INVALID.getMessage());
        }

        if (result.contains("UNABLE")) {
            throw new ResponseStatusException(VoteExceptionEnum.CPF_UNABLE_TO_VOTE.getHttpStatus(),
                    VoteExceptionEnum.CPF_UNABLE_TO_VOTE.getMessage());
        }
    }

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

            if (associate == null) {
                throw new ResponseStatusException(VoteExceptionEnum.ASSOCIATE_NOT_FOUND.getHttpStatus(),
                        VoteExceptionEnum.ASSOCIATE_NOT_FOUND.getMessage());
            }

            if (alreadyVoted(associate, votingSession)) {
                throw new ResponseStatusException(VoteExceptionEnum.ALREADY_VOTED.getHttpStatus(),
                        VoteExceptionEnum.ALREADY_VOTED.getMessage());
            }

            checkCpfStatus(associate.getCpf());

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

    public void sendResult(String message) {
        log.info("Sending message to Rabbit...");
        rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, "foo.bar.baz", message);
    }

    private void scheduleEndSession(long id, int minutesToRun) throws SchedulerException {
        // job
        JobDetail job = JobBuilder.newJob(QuartzJob.class).build();
        job.getJobDataMap().put("idTopic", id);

        // trigger
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .startAt(futureDate(minutesToRun, DateBuilder.IntervalUnit.MINUTE))
                .build();

        // schedule it
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory = new AutowiringSpringBeanJobFactory();
        autowiringSpringBeanJobFactory.setApplicationContext(applicationContext);
        scheduler.setJobFactory(autowiringSpringBeanJobFactory);

        scheduler.start();
        scheduler.scheduleJob(job, trigger);

    }

}
