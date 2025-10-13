package com.example.dat250demo.pollapp.dto;

import java.util.List;

public record CreatePollRequest(String question, List<String> options) {
}
