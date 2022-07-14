package com.shintheo.dgae.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.AnnouncementDto;

public interface AnnouncementRepository extends CrudRepository<AnnouncementDto, UUID> {

	List<AnnouncementDto> findAllByOrderByIdDesc();

	List<AnnouncementDto> findByDeparturetownOrDestinationtown(String departureTown, String destinationTown);

	@Transactional
	@Modifying
	@Query("update AnnouncementDto u set u.status = :status where u.id = :id")
	void disableAnnouncement(@Param(value = "id") UUID id, @Param(value = "status") Status status);

	List<AnnouncementDto> findByUserDtoIdAndStatus(UUID id, Status status);

	List<AnnouncementDto> findByStatus(Status status);

	List<AnnouncementDto> findByStatusAndValidation(Status status, boolean validation);

}
