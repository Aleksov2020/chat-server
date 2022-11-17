package com.app.chatserver.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "deleted_chat_rooms")
public class DeletedChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="chat_room_id", referencedColumnName = "id")
    private ChatRoom chatRoom;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}
