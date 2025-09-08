package com.example.dat250demo.pollapp.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;

public class VoteDtos {
    // request body when casting/changing a vote
    public record Cast(@NotBlank String username, @Min(1) int optionOrder) {}

    // response when reading a vote
    public record View(int pollId, String username, int optionOrder, Instant publishedAt) {}
}
