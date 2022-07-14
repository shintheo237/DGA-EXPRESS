package com.shintheo.dgae.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shintheo.dgae.common.ErrorMessage;
import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.AnnouncementDto;
import com.shintheo.dgae.exceptions.NotFoundException;
import com.shintheo.dgae.persistence.AnnouncementRepository;
import com.shintheo.dgae.service.AnnouncementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementRepository announcementRepository;

	@Override
	public void addAnnouncement(AnnouncementDto announcementDto) {
		log.info("Adding Announcement {}", announcementDto);
		announcementDto.setStatus(Status.ENABLED);
		announcementRepository.save(announcementDto);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public List<AnnouncementDto> getAllValidAnnouncements() {
		log.info("Fetching all valid announcement");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
		List<AnnouncementDto> announcementDtos = announcementRepository.findByStatusAndValidation(Status.ENABLED, true);
		for (AnnouncementDto announcement : announcementDtos) {
			if (announcement.getDeparturedate().equals(simpleDateFormat.format(new Date(System.currentTimeMillis())))) {
				announcementDtos.remove(announcement);
				log.info("**************** Im removing well");
			}
		}		
		return announcementDtos;
	}

	@Override
	public void addAllAnnouncements(List<AnnouncementDto> announcements) {
		log.info("Update list annoucements = {}", announcements);
		announcementRepository.saveAll(announcements);
	}

	@Override
	public AnnouncementDto getAnnoucement(UUID announcementId) {
		log.info("Fetching announcement by Id = {}", announcementId);
		AnnouncementDto announcementDto = announcementRepository.findById(announcementId).get();
		if (announcementDto.equals(null))
			throw new NotFoundException(ErrorMessage.NOTFOUNDEXCEPTION);
		return announcementDto;
	}

	@Override
	public void updateAnnoucement(AnnouncementDto announcementDto) {
		log.info("Update an annoucement = {}", announcementDto);
		announcementRepository.save(announcementDto);
	}

	@Override
	public void deleteAnnoucement(UUID announcementId) {
		log.info("Delete an announcement = {}", announcementId);
		announcementRepository.disableAnnouncement(announcementId, Status.DISABLED);
	}

	@Override
	public List<AnnouncementDto> getAnnouncementsByUser(UUID userId) {
		log.info("Fetching annoucement by user = {}", userId);
		try {
			List<AnnouncementDto> validAnnouncements = announcementRepository.findByUserDtoIdAndStatus(userId,
					Status.ENABLED);
			return validAnnouncements;
		} catch (Exception e) {
			throw new NotFoundException(ErrorMessage.NOTFOUNDEXCEPTION);
		}
	}

	@Override
	public List<AnnouncementDto> searchAnnouncements(String departure_town, String destination_town) {
		log.info("Searching announcements by departure town = {} and destination town {}", departure_town,
				destination_town);
		try {
			List<AnnouncementDto> announcements = announcementRepository
					.findByDeparturetownOrDestinationtown(departure_town, destination_town);
			for (AnnouncementDto announcement : announcements) {
				if (announcement.getStatus().equals(Status.DISABLED)) {
					announcements.remove(announcement);
				}
			}
			return announcements;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<AnnouncementDto> getAllAnnouncements() {
		log.info("Fetching all announcements for Amin");
		List<AnnouncementDto> announcementDtos = announcementRepository.findByStatus(Status.ENABLED);
		return announcementDtos;
	}

	@Override
	public void changeValidation(UUID id) {
		log.info("Change announcement validation by id = {}", id);
		AnnouncementDto announcementDto = this.getAnnoucement(id);
		if (announcementDto.isValidation()) {
			announcementDto.setValidation(false);
		} else {
			announcementDto.setValidation(true);
		}
		announcementRepository.save(announcementDto);
	}

}
