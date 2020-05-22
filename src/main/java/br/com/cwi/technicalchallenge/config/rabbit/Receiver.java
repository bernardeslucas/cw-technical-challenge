package br.com.cwi.technicalchallenge.config.rabbit;

import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        log.info("Received <" + message + ">");
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}