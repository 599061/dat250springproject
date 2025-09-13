package com.example.dat250demo.pollapp.service;

import com.example.dat250demo.pollapp.domains.*;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Component
public class PollManager {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<Integer, Poll> polls = new ConcurrentHashMap<>();
    private final Map<Integer, Map<String, Vote>> votes = new ConcurrentHashMap<>();
    private final AtomicInteger seq = new AtomicInteger(1);

    public Optional<User> findUser(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public Collection<User> listUsers() {
        return users.values();
    }

    public Optional<User> createUser(String username, String email) {
        if (users.containsKey(username)) return Optional.empty();
        User u = new User(username, email);
        users.put(username, u);
        return Optional.of(u);
    }

    public Optional<User> updateUserEmail(String username, String email) {
        User u = users.get(username);
        if (u == null) return Optional.empty();
        u.setEmail(email);
        return Optional.of(u);
    }

    public boolean deleteUser(String username) {
        return users.remove(username) != null;
    }

    public int createPoll(String question, Instant closesAt, String creatorUsername, List<VoteOption> options) {
        User creator = users.get(creatorUsername);
        if (creator == null) throw new NoSuchElementException("Creator not found");

        var orders = options.stream().map(VoteOption::getOrder).collect(Collectors.toSet());
        if (orders.size() != options.size() || orders.stream().anyMatch(o -> o < 1))
            throw new IllegalArgumentException("Options must have unique order >= 1");

        LinkedHashSet<VoteOption> set = options.stream()
                .sorted(Comparator.comparingInt(VoteOption::getOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Poll p = new Poll(question, set, creator);
        p.setClosesAt(closesAt);

        int id = seq.getAndIncrement();
        polls.put(id, p);
        votes.put(id, new ConcurrentHashMap<>());
        creator.getCreatedPolls().add(p);

        return id;
    }

    public Collection<Map.Entry<Integer, Poll>> listPolls() {
        return polls.entrySet();
    }

    public Optional<Poll> findPoll(int id) {
        return Optional.ofNullable(polls.get(id));
    }

    public Optional<Poll> updatePoll(int id, String question, Instant closesAt, List<VoteOption> options) {
        Poll p = polls.get(id);
        if (p == null) return Optional.empty();

        p.setQuestion(question);
        p.setClosesAt(closesAt);

        var set = options.stream()
                .sorted(Comparator.comparingInt(VoteOption::getOrder))
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
        p.setOptions(set);

        var validOrders = set.stream().map(VoteOption::getOrder).collect(Collectors.toSet());
        votes.getOrDefault(id, Map.of()).entrySet()
                .removeIf(e -> !validOrders.contains(e.getValue().getOptionOrder()));

        return Optional.of(p);
    }

    public boolean deletePoll(int id) {
        votes.remove(id);
        return polls.remove(id) != null;
    }

    public List<Map.Entry<VoteOption, Long>> tally(int pollId) {
        Poll p = polls.get(pollId);
        if (p == null) return List.of();

        Map<Integer, Long> counts = new HashMap<>();
        for (VoteOption o : p.getOptions()) counts.put(o.getOrder(), 0L);
        votes.getOrDefault(pollId, Map.of()).values()
                .forEach(v -> counts.computeIfPresent(v.getOptionOrder(), (k, c) -> c + 1));

        return p.getOptions().stream()
                .sorted(Comparator.comparingInt(VoteOption::getOrder))
                .map(o -> Map.entry(o, counts.getOrDefault(o.getOrder(), 0L)))
                .toList();
    }

    public Optional<Vote> castOrChangeVote(int pollId, String username, int optionOrder) {
        Poll p = polls.get(pollId);
        if (p == null) return Optional.empty();

        if (Instant.now().isAfter(p.getClosesAt()))
            throw new IllegalStateException("Poll is closed");

        if (!users.containsKey(username))
            throw new NoSuchElementException("User not found");

        boolean optionExists = p.getOptions().stream()
                .anyMatch(o -> o.getOrder() == optionOrder);
        if (!optionExists)
            throw new NoSuchElementException("Option not in poll");

        Vote v = new Vote(username, optionOrder, Instant.now());
        votes.computeIfAbsent(pollId, k -> new ConcurrentHashMap<>()).put(username, v);
        return Optional.of(v);
    }

    public Optional<Vote> getUsersVote(int pollId, String username) {
        return Optional.ofNullable(votes.getOrDefault(pollId, Map.of()).get(username));
    }
}