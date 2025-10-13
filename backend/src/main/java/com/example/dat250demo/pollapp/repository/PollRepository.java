package com.example.dat250demo.pollapp.repository;

import com.example.dat250demo.pollapp.jpa.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
}
