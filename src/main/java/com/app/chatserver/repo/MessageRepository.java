package com.app.chatserver.repo;

import java.util.List;
import java.util.Optional;

import com.app.chatserver.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends JpaRepository <Message, Integer> {
	List <Message> findTop50ByChat_IdOrderByDateCreate(Integer chatId);

	List <Message> findAllByChat_Id(Integer chatId, Pageable pageable);
	Optional<Message> findMessageById(Integer messageId);
	boolean existsById(Integer messageId);
	void deleteMessageById(Integer messageId);
	List<Message> findMessagesByChat_Id(Integer chatId);
}
