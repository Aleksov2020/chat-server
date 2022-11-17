package com.app.chatserver.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name="content")
    private String content;
}
