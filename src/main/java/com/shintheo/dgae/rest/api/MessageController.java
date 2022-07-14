package com.shintheo.dgae.rest.api;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shintheo.dgae.domaine.MessageDto;
import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.service.impl.MessageServiceImpl;
import com.shintheo.dgae.service.impl.UserDtoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "MessageDto")
public class MessageController {

	@Autowired
	private MessageServiceImpl messageServiceImpl;
	@Autowired
	private UserDtoServiceImpl userDtoServiceImpl;

	@RequestMapping(method = RequestMethod.POST, value = "/add/message")
	@Secured({ "ROLE_CLIENT" })
	@Operation(summary = "Add Message", description = "Add a new message ")
	public void addMessage(@RequestBody MessageDto message) {
		messageServiceImpl.addMessage(message);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/chat/messages/{idreceiver}")
	@Secured({ "ROLE_CLIENT" })
	@Operation(summary = "Get messages", description = "Get messages between Sender an receiver")
	public List<MessageDto> getMessageBetween(Principal principal, UUID idreceiver) {
		UserDto Sender = userDtoServiceImpl.loadUserByEmail(principal.getName());
		UserDto receiver = userDtoServiceImpl.getUser(idreceiver);
		return messageServiceImpl.getMessagesBetween(Sender, receiver);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/message/{idmessage}/messages")
	@Secured({ "ROLE_CLIENT" })
	@Operation(summary = "Delete Message", description = "Delete specific message by Id")
	public void deleteMessage(UUID idmessage) {
		messageServiceImpl.deletemessage(idmessage);
	}

}
