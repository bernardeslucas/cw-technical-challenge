package br.com.cwi.technicalchallenge;

import br.com.cwi.technicalchallenge.controller.v1.request.TopicRequest;
import br.com.cwi.technicalchallenge.controller.v1.response.TopicResponse;
import br.com.cwi.technicalchallenge.domain.Topic;
import br.com.cwi.technicalchallenge.repository.TopicRepository;
import br.com.cwi.technicalchallenge.repository.VotingSessionRepository;
import br.com.cwi.technicalchallenge.service.TopicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ExtendWith(SpringExtension.class)
public class TopicServiceTest {

    private static TopicRequest topicRequest1;
    private static Topic topic1;
    private static TopicResponse topicResponse1;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @InjectMocks
    private TopicService topicService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        topicRequest1 = new TopicRequest("desc 1", "title 1");
        topic1 = new Topic((long) 1, "desc 1", "title 1");
        topicResponse1 = new TopicResponse(1, "title1", "desc 1", "result 1");
    }

    @Test
    public void findAllWhenEmpty() {
        Mockito.when(topicRepository.findAll()).thenReturn(Arrays.asList());
        assertThat(topicService.findAll().size(), is(0));
        Mockito.verify(topicRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findAllWhenRecord() {

        Mockito.when(topicRepository.findAll()).thenReturn(Arrays.asList(topic1));

        Mockito.when(objectMapper.convertValue(Mockito.any(), Mockito.any(Class.class))).thenReturn(topicResponse1);

        assertThat(topicService.findAll().size(), is(1));
        assertThat(topicService.findAll().get(0).getDescription(), is(topicResponse1.getDescription()));

        Mockito.verify(topicRepository, Mockito.times(2)).findAll();

    }

    @Test
    public void findById() {
        Mockito.when(topicRepository.findById(1L)).thenReturn(topic1);
        Mockito.when(objectMapper.convertValue(Mockito.any(), Mockito.any(Class.class))).thenReturn(topicResponse1);
        assertThat(topicService.findById(1L).getDescription(), is(topicResponse1.getDescription()));
        Mockito.verify(topicRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void create() {
        Mockito.when(topicRepository.save(topic1)).thenReturn(topic1);

        Mockito.when(objectMapper.convertValue(Mockito.any(), Mockito.any(Class.class))).thenReturn(topic1);

        assertThat(topicService.save(topicRequest1).getDescription(), is(topic1.getDescription()));

        Mockito.verify(topicRepository, Mockito.times(1)).save(topic1);
    }

}
