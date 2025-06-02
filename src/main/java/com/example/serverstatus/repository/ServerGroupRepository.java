package com.example.serverstatus.repository;

import com.example.serverstatus.model.ServerGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerGroupRepository extends JpaRepository<ServerGroup, Long> {
}
