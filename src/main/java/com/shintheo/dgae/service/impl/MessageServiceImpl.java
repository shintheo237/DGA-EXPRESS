package com.shintheo.dgae.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shintheo.dgae.common.ErrorMessage;
import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.MessageDto;
import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.exceptions.EmptyValueException;
import com.shintheo.dgae.persistence.MessageRepository;
import com.shintheo.dgae.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	public MessageRepository messageRepository;

	@Override
	public void addMessage(MessageDto messageDto) {
		log.info("Add new Message {}", messageDto);
		messageRepository.save(messageDto);
	}

	@Override
	public void deletemessage(UUID id) {
		log.info("Fetching Message by id = {}", id);
		messageRepository.disableMessage(id, Status.DISABLED);
	}

	@Override
	public List<MessageDto> getMessagesBetween(UserDto sender, UserDto receiver) throws EmptyValueException {
		log.info("fetching chat between {} and {}", sender, receiver);
		List<UserDto> users = Arrays.asList(sender, receiver);
		List<MessageDto> messages = messageRepository.findBySendermessageInAndReceivermessageIn(users, users);
		if (messages.isEmpty())
			throw new EmptyValueException(ErrorMessage.CHATEXCEPTION);
		return messages;
	}

}
