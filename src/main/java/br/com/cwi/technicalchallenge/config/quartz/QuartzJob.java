package br.com.cwi.technicalchallenge.config.quartz;

import br.com.cwi.technicalchallenge.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class QuartzJob implements Job {

    @Autowired
    private TopicService service;

    @Override
    public void execute(JobExecutionContext exc) throws JobExecutionException {
//        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        JobDataMap data = exc.getJobDetail().getJobDataMap();
        long id = data.getLong("idTopic");

        service.sendResult(service.findById(id).getResult());

        try {
            exc.getScheduler().shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
