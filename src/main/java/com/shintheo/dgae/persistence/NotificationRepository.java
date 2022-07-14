package com.shintheo.dgae.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.NotificationDto;

public interface NotificationRepository extends CrudRepository<NotificationDto, UUID> {
	List<NotificationDto> findAllByOrderByIdDesc();

	List<NotificationDto> findByUserDtoId(UUID id);

//	List<NotificationDto> findByReservationDtoId(UUID id);

	@Transactional
	@Modifying
	@Query("update NotificationDto u set u.status = :status where u.id = :id")
	void disableNotification(@Param(value = "id") UUID id, @Param(value = "status") Status status);
}
