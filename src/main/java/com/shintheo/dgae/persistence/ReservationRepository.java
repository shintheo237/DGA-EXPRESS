package com.shintheo.dgae.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.ReservationDto;

public interface ReservationRepository extends CrudRepository<ReservationDto, UUID> {

	List<ReservationDto> findByUserDtoIdAndStatus(UUID id, Status status);

	@Transactional
	@Modifying
	@Query("update ReservationDto u set u.status = :status where u.id = :id")
	void disableReservation(@Param(value = "id") UUID id, @Param(value = "status") Status status);

	@Query("select r from ReservationDto r where r.status = :status order by r.id desc")
	List<ReservationDto> findEnabledReservations(@Param(value = "status") Status status);

}
