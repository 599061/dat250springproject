package com.example.dat250demo.pollapp.jpa.entities;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users") // PollsTest does: select count(id) from users
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Poll> created = new LinkedHashSet<>();

    protected User() {} // JPA

    /** Creates a new User; id is DB-generated. */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /** Creates a Poll owned by this user. */
    public Poll createPoll(String question) {
        Poll p = new Poll(question, this);
        created.add(p);
        return p;
    }

    /** Creates a Vote for the given option. */
    public Vote voteFor(VoteOption option) {
        return new Vote(this, option);
    }

    // getters/setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Poll> getCreated() { return created; }
}
