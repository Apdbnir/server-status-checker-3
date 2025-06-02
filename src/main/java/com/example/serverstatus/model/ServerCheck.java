package com.example.serverstatus.model;

import jakarta.persistence.*;

@Entity
public class ServerCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String status;
    private int code;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ServerGroup group;

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public ServerGroup getGroup() { return group; }
    public void setGroup(ServerGroup group) { this.group = group; }
}
