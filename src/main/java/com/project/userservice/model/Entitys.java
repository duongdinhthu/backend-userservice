package com.project.userservice.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Entitys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Entitys() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
