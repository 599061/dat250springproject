package com.example.dat250demo.pollapp.messaging;

import com.example.dat250demo.pollapp.jpa.entities.Poll;
import com.example.dat250demo.pollapp.jpa.entities.VoteOption;
import com.example.dat250demo.pollapp.repository.PollRepository;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class VoteListener {

    private final PollRepository repo;
    private final UpdatePublisher updatePublisher;

    public VoteListener(PollRepository repo, UpdatePublisher updatePublisher) {
        this.repo = repo;
        this.updatePublisher = updatePublisher;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "polls-all-votes", durable = "true"),
            exchange = @Exchange(value = "polls", type = "topic", durable = "true"),
            key = "poll.*.vote"
    ))
    public void onVote(VoteEvent event) {
        repo.findById(event.pollId()).ifPresent(poll -> {
            poll.incrementOptionCount(event.optionId());
            repo.save(poll);
            updatePublisher.publishPollUpdated(event.pollId(), buildTotalsPayload(poll));
        });
    }

    private Map<String, Object> buildTotalsPayload(Poll poll) {
        Map<String, Object> body = new HashMap<>();
        body.put("pollId", poll.getId());

        body.put("totals", poll.getOptions().stream().collect(Collectors.toMap(
                o -> String.valueOf(o.getId()),
                VoteOption::getVoteCount
        )));

        return body;
    }
}
