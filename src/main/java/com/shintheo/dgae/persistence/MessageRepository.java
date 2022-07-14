package com.shintheo.dgae.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.MessageDto;
import com.shintheo.dgae.domaine.UserDto;

public interface MessageRepository extends CrudRepository<MessageDto, UUID> {

	List<MessageDto> findAllByOrderByIdDesc();

	List<MessageDto> findByReservationDtoIdAndStatus(UUID id, Status status);

	List<MessageDto> findBySendermessageInAndReceivermessageIn(List<UserDto> sender, List<UserDto> receiver );

	@Transactional
	@Modifying
	@Query("update MessageDto u set u.status = :status where u.id = :id")
	void disableMessage(@Param(value = "id") UUID id, @Param(value = "status") Status status);
}
