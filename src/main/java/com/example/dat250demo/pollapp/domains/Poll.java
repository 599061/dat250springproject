package com.example.dat250demo.pollapp.domains;

import java.time.Instant;
import java.util.Set;

public class Poll {

    private String question;
    private Set<VoteOption> options;
    private User creator;
    private Instant closesAt;

    public Poll() { }

    public Poll(String question, Set<VoteOption> options, User creator) {
        this.question = question;
        this.options = options;
        this.creator = creator;
    }

    public Instant getClosesAt() {
        return closesAt;
    }

    public void setClosesAt(Instant closesAt) {
        this.closesAt = closesAt;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<VoteOption> getOptions() {
        return options;
    }

    public void setOptions(Set<VoteOption> options) {
        this.options = options;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}