package com.shintheo.dgae.service;

import java.util.List;
import java.util.UUID;

import com.shintheo.dgae.domaine.MessageDto;
import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.exceptions.EmptyValueException;

public interface MessageService {

	void addMessage(MessageDto messageDto);

	List<MessageDto> getMessagesBetween(UserDto sender, UserDto receiver) throws EmptyValueException;

	void deletemessage(UUID id);
}
