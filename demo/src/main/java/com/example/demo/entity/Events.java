package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Events {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private Integer cost;
    @Column
    private Integer duration;


}
