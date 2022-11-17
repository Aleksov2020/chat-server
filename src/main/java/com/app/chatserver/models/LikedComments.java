package com.app.chatserver.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "likedComments")
public class LikedComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="comment_id", referencedColumnName = "id")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}
