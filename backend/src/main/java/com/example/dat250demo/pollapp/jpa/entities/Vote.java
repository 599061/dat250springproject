package com.example.dat250demo.pollapp.jpa.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "votes")
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // JPQL in the test: v.votesOn -> VoteOption
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private VoteOption votesOn;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "voted_by_id")
    private User votedBy;

    @Column(nullable = false)
    private Instant publishedAt = Instant.now();

    protected Vote() {}

    public Vote(User voter, VoteOption option) {
        this.votedBy = voter;
        this.votesOn = option;
        this.poll = option.getPoll();
    }

    // getters/setters
    public Long getId() { return id; }
    public VoteOption getVotesOn() { return votesOn; }
    public void setVotesOn(VoteOption votesOn) { this.votesOn = votesOn; }
    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }
    public User getVotedBy() { return votedBy; }
    public void setVotedBy(User votedBy) { this.votedBy = votedBy; }
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }
}
