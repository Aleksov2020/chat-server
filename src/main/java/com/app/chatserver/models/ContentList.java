package com.app.chatserver.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="content_list")
public class ContentList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="content_id", referencedColumnName = "id")
    private Content content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="media_id", referencedColumnName = "id")
    private Media media;
}
