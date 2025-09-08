package com.example.dat250demo.pollapp.web;

import jakarta.validation.Valid;
import com.example.dat250demo.pollapp.dto.UserDtos;
import com.example.dat250demo.pollapp.service.PollManager;
import com.example.dat250demo.pollapp.domains.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final PollManager mgr;
    public UserController(PollManager mgr) { this.mgr = mgr; }

    @GetMapping
    public List<UserDtos.View> list() {
        return mgr.listUsers().stream().map(this::view).toList();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserDtos.Create in) {
        return mgr.createUser(in.username(), in.email())
                .<ResponseEntity<?>>map(u -> ResponseEntity.created(URI.create("/api/users/" + u.getUsername())).body(view(u)))
                .orElseGet(() -> ResponseEntity.status(409).body("Username already exists"));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDtos.View> get(@PathVariable String username) {
        return mgr.findUser(username).map(u -> ResponseEntity.ok(view(u))).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDtos.View> update(@PathVariable String username, @RequestBody @Valid UserDtos.Update in) {
        return mgr.updateUserEmail(username, in.email())
                .map(u -> ResponseEntity.ok(view(u))).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        return mgr.deleteUser(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private UserDtos.View view(User u) {
        return new UserDtos.View(u.getUsername(), u.getEmail());
    }
}