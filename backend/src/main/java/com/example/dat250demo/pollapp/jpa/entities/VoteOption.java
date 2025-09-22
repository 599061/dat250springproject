package com.example.dat250demo.pollapp.jpa.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "vote_options",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_poll_order",
                columnNames = {"poll_id", "presentation_order"}
        )
)
public class VoteOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // JPQL in the test refers to "presentationOrder"
    @Column(name = "presentation_order", nullable = false)
    private int presentationOrder;

    @Column(nullable = false)
    private String caption;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    protected VoteOption() {}

    public VoteOption(int presentationOrder, String caption, Poll poll) {
        this.presentationOrder = presentationOrder;
        this.caption = caption;
        this.poll = poll;
    }

    // getters/setters
    public Long getId() { return id; }
    public int getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(int presentationOrder) { this.presentationOrder = presentationOrder; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }
}
