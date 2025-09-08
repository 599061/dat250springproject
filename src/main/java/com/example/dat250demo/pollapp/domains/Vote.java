package com.example.dat250demo.pollapp.domains;

import java.time.Instant;

public class Vote {

    private String username;
    private int optionOrder;
    private Instant publishedAt;

    public Vote() {}

    public Vote(String username, int optionOrder, Instant publishedAt) {
        this.username = username; this.optionOrder = optionOrder; this.publishedAt = publishedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOptionOrder() {
        return optionOrder;
    }

    public void setOptionOrder(int optionOrder) {
        this.optionOrder = optionOrder;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }
}