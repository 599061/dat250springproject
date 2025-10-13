package com.example.dat250demo.pollapp.service;

import com.example.dat250demo.pollapp.jpa.entities.Poll;
import com.example.dat250demo.pollapp.jpa.entities.VoteOption;
import com.example.dat250demo.pollapp.repository.PollRepository;
import com.example.dat250demo.pollapp.dto.CreatePollRequest;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.example.dat250demo.pollapp.config.RabbitConfig.POLL_EXCHANGE;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final RabbitAdmin rabbitAdmin;
    private final TopicExchange exchange;

    public PollService(PollRepository pollRepository, RabbitAdmin rabbitAdmin, @Qualifier("pollsExchange") TopicExchange exchange) {
        this.pollRepository = pollRepository;
        this.rabbitAdmin = rabbitAdmin;
        this.exchange = exchange;
    }

    public Poll createPoll(CreatePollRequest req) {
        Poll poll = new Poll();
        poll.setQuestion(req.question());
        poll.setOptions(new ArrayList<>());
        for (String text : req.options()) {
            VoteOption opt = new VoteOption();
            opt.setCaption(text);
            opt.setVoteCount(0);
            opt.setPoll(poll);
            poll.getOptions().add(opt);
        }

        poll = pollRepository.save(poll);

        String queueName = "poll." + poll.getId();
        Queue queue = QueueBuilder.durable(queueName).build();
        rabbitAdmin.declareQueue(queue);

        String routingKey = "poll." + poll.getId() + ".vote";
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);

        return poll;
    }
}
