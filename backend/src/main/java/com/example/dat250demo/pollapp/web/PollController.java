package com.example.dat250demo.pollapp.web;


import jakarta.validation.Valid;
import com.example.dat250demo.pollapp.dto.PollDtos;
import com.example.dat250demo.pollapp.service.PollManager;
import com.example.dat250demo.pollapp.domains.Poll;
import com.example.dat250demo.pollapp.domains.VoteOption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/api/polls")
public class PollController {

    private final PollManager mgr;

    public PollController(PollManager mgr) { this.mgr = mgr; }

    @GetMapping
    public List<PollDtos.View> list() {
        return mgr.listPolls().stream()
                .map(e -> view(e.getKey(), e.getValue()))
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid PollDtos.Create in) {
        try {
            List<VoteOption> opts = in.options().stream()
                    .map(o -> new VoteOption(o.order(), o.caption()))
                    .toList();
            int id = mgr.createPoll(in.question(), in.closesAt(), in.creatorUsername(), opts);

            return mgr.findPoll(id)
                    .map(p -> ResponseEntity.created(URI.create("/api/polls/" + id)).body(view(id, p)))
                    .orElse(ResponseEntity.internalServerError().build());
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollDtos.View> get(@PathVariable int id) {
        return mgr.findPoll(id)
                .map(p -> ResponseEntity.ok(view(id, p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PollDtos.View> update(@PathVariable int id, @RequestBody @Valid PollDtos.Update in) {
        List<VoteOption> opts = in.options().stream()
                .map(o -> new VoteOption(o.order(), o.caption()))
                .toList();
        return mgr.updatePoll(id, in.question(), in.closesAt(), opts)
                .map(p -> ResponseEntity.ok(view(id, p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return mgr.deletePoll(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<PollDtos.Result>> results(@PathVariable int id) {
        return mgr.findPoll(id)
                .map(p -> {
                    var res = mgr.tally(id).stream()
                            .map(e -> new PollDtos.Result(
                                    e.getKey().getOrder(),
                                    e.getKey().getCaption(),
                                    e.getValue()))
                            .toList();
                    return ResponseEntity.ok(res);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private PollDtos.View view(int id, Poll p) {
        var options = p.getOptions().stream()
                .sorted(Comparator.comparingInt(VoteOption::getOrder))
                .map(o -> new PollDtos.OptionView(o.getOrder(), o.getCaption()))
                .toList();
        return new PollDtos.View(id, p.getQuestion(), p.getClosesAt(), p.getCreator().getUsername(), options);
    }
}