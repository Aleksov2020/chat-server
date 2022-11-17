package com.app.chatserver.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="avatar")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "size")
    private Long size;
    @Column(name="path")
    private String path;
}
