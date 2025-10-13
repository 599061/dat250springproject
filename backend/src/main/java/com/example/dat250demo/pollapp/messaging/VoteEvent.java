package com.example.dat250demo.pollapp.messaging;

public record VoteEvent(Long pollId, String optionId) {
}
