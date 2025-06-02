package com.example.serverstatus.repository;

import com.example.serverstatus.model.ServerCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerCheckRepository extends JpaRepository<ServerCheck, Long> {
}
