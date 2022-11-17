package com.app.chatserver.models;

import javax.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_id", referencedColumnName = "id")
	private ChatRoom chat;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", referencedColumnName = "id")
	private User sender;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", referencedColumnName = "id")
	private Content content;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "media_id", referencedColumnName = "id")
	private Media media;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "forward_id", referencedColumnName = "id", nullable = true)
	private Message forward;

	@Column(name = "date_create")
	private Date dateCreate;

	//TODO  post entity
}
