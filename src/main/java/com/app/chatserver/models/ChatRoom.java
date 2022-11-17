package com.app.chatserver.models;

import javax.persistence.*;

import com.app.chatserver.enums.ChatType;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "media_id", referencedColumnName = "id")
	private Media media;
	@Column(name = "chat_name")
	private String chatName;
	@Column(name = "chat_type")
	private ChatType chatType;
	@Column(name = "date_start")
	private Date dateStart;
	@Column(name = "date_last_update")
	private Date dateLastUpdate;

}
