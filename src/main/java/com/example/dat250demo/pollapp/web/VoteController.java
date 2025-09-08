package com.example.dat250demo.pollapp.web;

import jakarta.validation.Valid;
import com.example.dat250demo.pollapp.dto.VoteDtos;
import com.example.dat250demo.pollapp.service.PollManager;
import com.example.dat250demo.pollapp.domains.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/polls/{pollId}/votes")
public class VoteController {

    private final PollManager mgr;

    public VoteController(PollManager mgr) {
        this.mgr = mgr;
    }

    @PostMapping
    public ResponseEntity<?> cast(@PathVariable int pollId, @RequestBody @Valid VoteDtos.Cast in) {
        try {
            return mgr.castOrChangeVote(pollId, in.username(), in.optionOrder())
                    .<ResponseEntity<?>>map(v -> ResponseEntity.ok(view(pollId, v)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<VoteDtos.View> getForUser(@PathVariable int pollId, @RequestParam String username) {
        return mgr.getUsersVote(pollId, username).map(v -> ResponseEntity.ok(view(pollId, v))).orElse(ResponseEntity.notFound().build());
    }

    private VoteDtos.View view(int pollId, Vote v) {
        return new VoteDtos.View(pollId, v.getUsername(), v.getOptionOrder(), v.getPublishedAt());
    }
}