package com.app.chatserver.controllers;

import com.app.chatserver.dto.*;
import com.app.chatserver.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@Slf4j
public class ChatController {
	private final ChatRoomService chatRoomService;
	private final UserService userService;
	private final JwtTokenService jwtTokenService;
	private final ChatService chatService;

    public ChatController(ChatRoomService chatRoomService,
						  UserService userService,
						  JwtTokenService jwtTokenService,
						  ChatService chatService) {
        this.chatRoomService = chatRoomService;
		this.userService = userService;
		this.jwtTokenService = jwtTokenService;
		this.chatService = chatService;
    }

	@PostMapping("/chat/v1.0/sendMessage/{accessToken}")
	public ResponseEntity<ResponseFirstMessage> sendMessage( @PathVariable String accessToken,
														 	 @RequestBody InputMessage message) throws IOException {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				chatService.sendNewMessage(
						message,
						(Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")
				),
				HttpStatus.OK
		);
	}

	@GetMapping("/chat/v1.0/getChatRoom/{accessToken}/{chatRoomId}")
	public ResponseEntity<ResponseChatRoom> getChatRoom( @PathVariable String accessToken,
														 @PathVariable Integer chatRoomId) throws IOException {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				chatRoomService.serializeChatRoomToSend(
						chatRoomService.findChatRoomById(chatRoomId),
						(Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")
				),
				HttpStatus.OK
		);
	}

	/* Not working now
	@MessageMapping("/createGroup")
	public void createGroup( @Payload InputGroupChat inputGroupChat) {
		//TODO set user admin in chat
		chatService.createGroupChat(
				inputGroupChat
		);
	}

	@MessageMapping("/newGroupMessage")
	public void newGroupMessage( @Payload InputMessage message,
							     Principal principal) throws IOException {
		//TODO set user admin in chat
		chatService.sendNewGroupMessage(
				message,
				Integer.valueOf(principal.getName())
		);
	} */

	@GetMapping("/chat/v1.0/getMessages/{accessToken}")
	public ResponseEntity<ResponseMessageList> getMessages( @PathVariable String accessToken,
											   				@RequestParam Integer chatId,
															@RequestParam(defaultValue = "0", required = false) Integer page,
															@RequestParam(defaultValue = "50", required = false) Integer pageSize) throws IOException {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				chatService.getMessages(
						chatId,
						(Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id"),
						page,
						pageSize
				),
				HttpStatus.OK
		);
	}

	@GetMapping("/chat/v1.0/getMessage/{accessToken}/{messageId}")
	public ResponseEntity<ResponseMessage> getMessage( @PathVariable String accessToken,
													   @PathVariable Integer messageId) throws IOException {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				chatService.getMessageById(
						messageId,
						(Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")
				),
				HttpStatus.OK
		);
	}

	@GetMapping("/chat/v1.0/getUserPhoto/{accessToken}/{userId}")
	public ResponseEntity<ResponseMedia> getUserPhoto( @PathVariable String accessToken,
													   @PathVariable Integer userId) {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				chatService.getUserPhoto(
						userId
				),
				HttpStatus.OK
		);
	}

	@GetMapping("/chat/v1.0/getChatRooms/{accessToken}")
	public ResponseEntity<ResponseChatRoomList> getChatRooms( @PathVariable String accessToken,
															  @RequestParam(defaultValue = "0", required = false) Integer page,
															  @RequestParam(defaultValue = "20", required = false) Integer pageSize) throws IOException {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				chatRoomService.findChatRoomsByUserIdAndSerialize(
						(Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id"),
						page,
						pageSize
				),
				HttpStatus.OK
		);
	}

	@GetMapping("/chat/v1.0/deleteMessage/{accessToken}/{messageId}")
	public ResponseEntity<Response> deleteMessage( @PathVariable String accessToken,
												   @PathVariable Integer messageId ) {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				new Response(
						chatService
								.moveMessageToDeletedForUser(
										messageId,
										(Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")
								)
				),
				HttpStatus.OK
		);
	}

	@GetMapping("/chat/v1.0/deleteChatRoom/{accessToken}/{chatId}")
	public ResponseEntity<Response> deleteChatRoom( @PathVariable String accessToken,
												    @PathVariable Integer chatId ) {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				new Response(
						//Возможно добавить удаление через дату удаления chat_Room.
						chatService.moveChatRoomToDeletedForUser(
								chatId,
								(Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")
						)
				),
				HttpStatus.OK
		);
	}

	@GetMapping("/chat/v1.0/userName/{accessToken}/{phone}")
	public ResponseEntity<Response> getUserNameByPhone( @PathVariable String accessToken,
														@PathVariable String phone) {
		jwtTokenService.validateToken(accessToken);
		return new ResponseEntity<>(
				new Response(
						chatService.getUserNameByPhone(
								phone
						)
				),
				HttpStatus.OK
		);
	}

//	@GetMapping("/chat/v1.0/searchMessageChat/{accessToken}")
//	public ResponseEntity<Response> searchMessageInChat( @PathVariable String accessToken,
//														 @PathVariable String messageContent,
//														 @RequestParam(defaultValue = "0", required = false) Integer page,
//														 @RequestParam(defaultValue = "25", required = false) Integer pageSize) {
//		jwtTokenService.validateToken(accessToken);
//		return new ResponseEntity<>(
//				new Response(
//
//				),
//				HttpStatus.OK
//		);
//	}
}

