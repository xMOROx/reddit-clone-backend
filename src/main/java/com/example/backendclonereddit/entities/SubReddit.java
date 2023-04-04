package com.example.backendclonereddit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class SubReddit {
    @Id
    @GeneratedValue
    private Long id;
}
