package com.shintheo.dgae.service;

import java.util.List;
import java.util.UUID;

import com.shintheo.dgae.domaine.AnnouncementDto;
import com.shintheo.dgae.exceptions.EmptyValueException;
import com.shintheo.dgae.exceptions.NotFoundException;

public interface AnnouncementService {

	void addAnnouncement(AnnouncementDto announcementDto);

	List<AnnouncementDto> getAllValidAnnouncements() throws EmptyValueException;

	List<AnnouncementDto> getAllAnnouncements() throws EmptyValueException;

	List<AnnouncementDto> getAnnouncementsByUser(UUID userId) throws NotFoundException;

	List<AnnouncementDto> searchAnnouncements(String departure_town, String destination_town) throws NotFoundException;

	AnnouncementDto getAnnoucement(UUID announcementId) throws NotFoundException;

	void updateAnnoucement(AnnouncementDto announcementDto);

	void deleteAnnoucement(UUID id);

	void addAllAnnouncements(List<AnnouncementDto> announcements);

	void changeValidation(UUID id);

}
