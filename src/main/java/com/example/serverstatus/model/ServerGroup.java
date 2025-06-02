package com.example.serverstatus.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ServerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServerCheck> checks;

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ServerCheck> getChecks() { return checks; }
    public void setChecks(List<ServerCheck> checks) { this.checks = checks; }
}
