package com.example.dat250demo.pollapp.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.List;

public class PollDtos {

    public record OptionIn(int order, @NotBlank String caption) {}

    public record Create(@NotBlank String question, @NotNull Instant closesAt,
                         @NotBlank String creatorUsername, @Size(min = 2) List<OptionIn> options) {}

    public record Update(@NotBlank String question, @NotNull Instant closesAt,
                         @Size(min = 2) List<OptionIn> options) {}

    public record OptionView(int order, String caption) {}

    public record View(int id, String question, Instant closesAt,
                       String creatorUsername, List<OptionView> options) {}

    public record Result(int order, String caption, long votes) {}
}
