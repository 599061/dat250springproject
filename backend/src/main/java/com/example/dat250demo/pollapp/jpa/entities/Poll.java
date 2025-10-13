package com.example.dat250demo.pollapp.jpa.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    private Instant closesAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("presentationOrder ASC")
    private List<VoteOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    public Poll() {}

    public Poll(String question, User createdBy) {
        this.question = question;
        this.createdBy = createdBy;
    }

    public VoteOption addVoteOption(String caption) {
        int order = options.size();
        VoteOption vo = new VoteOption(order, caption, this);
        options.add(vo);
        return vo;
    }

    public void incrementOptionCount(String optionIdOrText) {
        options.stream()
                .filter(o -> String.valueOf(o.getId()).equals(optionIdOrText) || o.getCaption().equals(optionIdOrText))
                .findFirst()
                .ifPresent(o -> o.setVoteCount(o.getVoteCount() + 1));
    }

    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public Instant getClosesAt() { return closesAt; }
    public void setClosesAt(Instant closesAt) { this.closesAt = closesAt; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public List<VoteOption> getOptions() { return options; }
    public void setOptions(List<VoteOption> options) { this.options = options; }
    public List<Vote> getVotes() { return votes; }
}
