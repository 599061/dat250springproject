package com.example.dat250demo.pollapp.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.example.dat250demo.pollapp.config.RabbitConfig.POLL_EXCHANGE;

@Component
public class UpdatePublisher {

    private final RabbitTemplate rabbit;

    public UpdatePublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishPollUpdated(Long pollId, Object payload) {
        rabbit.convertAndSend(POLL_EXCHANGE, "poll." + pollId + ".updated", payload);
    }
}
