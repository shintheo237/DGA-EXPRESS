package com.shintheo.dgae.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shintheo.dgae.common.ErrorMessage;
import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.AnnouncementDto;
import com.shintheo.dgae.domaine.MessageDto;
import com.shintheo.dgae.domaine.NotificationDto;
import com.shintheo.dgae.domaine.ReservationDto;
import com.shintheo.dgae.exceptions.EmptyValueException;
import com.shintheo.dgae.exceptions.NotFoundException;
import com.shintheo.dgae.persistence.AnnouncementRepository;
import com.shintheo.dgae.persistence.MessageRepository;
import com.shintheo.dgae.persistence.ReservationRepository;
import com.shintheo.dgae.service.ReservationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReservationServiceimpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private AnnouncementRepository announcementRepository;
	@Autowired
	private AnnouncementServiceImpl announcementServiceImpl;
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public void addReservation(ReservationDto reservationDto) {
		log.info("Adding a new reservation = {}", reservationDto);
		reservationDto.setStatus(Status.ENABLED);
		reservationDto.setDate(ReservationDto.addDate());
		reservationDto.setTotalprice(reservationDto.getAnnouncementDto().getPrice() * reservationDto.getQuantitykilo());
		AnnouncementDto announcementDto = announcementServiceImpl
				.getAnnoucement(reservationDto.getAnnouncementDto().getId());
		announcementDto
				.setQuantity(reservationDto.getAnnouncementDto().getQuantity() - reservationDto.getQuantitykilo());
		if (announcementDto.getQuantity() < 1) {
			announcementDto.setStatus(Status.DISABLED);
		}else if (announcementDto.getQuantity() < reservationDto.getQuantitykilo()) {
			throw new RuntimeException("reservation kilo is too much than Announcement kilo");
		}
		announcementRepository.save(announcementDto);
		reservationRepository.save(reservationDto);
	}

	@Override
	public void deleteReservation(UUID idReservationDto) {
		log.info("delete Reservation by id = {}", idReservationDto);
		reservationRepository.disableReservation(idReservationDto, Status.DISABLED);
		ReservationDto reservationDto = this.getReservationById(idReservationDto);
		reservationDto.getAnnouncementDto().setQuantity(reservationDto.getQuantitykilo());
		reservationDto.getAnnouncementDto().setStatus(Status.ENABLED);
		List<MessageDto> messages = messageRepository.findByReservationDtoIdAndStatus(idReservationDto, Status.ENABLED);
		messages.forEach(x -> x.setStatus(Status.DISABLED));
		messageRepository.saveAll(messages);
	}

	@Override
	public List<ReservationDto> getReservationByUser(UUID userid) {
		log.info("Fetching Reservation by user Id = {}", userid);
		List<ReservationDto> reservationDtos = reservationRepository.findByUserDtoIdAndStatus(userid, Status.ENABLED);
		if (reservationDtos.isEmpty())
			throw new NotFoundException(ErrorMessage.NOTFOUNDEXCEPTION);
		return reservationDtos;
	}

	@Override
	public List<ReservationDto> getAllReservation() {
		log.info("Fetching All Reservation");
		List<ReservationDto> reservationDtos = reservationRepository.findEnabledReservations(Status.ENABLED);
		if (reservationDtos.isEmpty())
			throw new EmptyValueException(ErrorMessage.EMPTYVALUEEXCEPTION);
		return reservationDtos;
	}

	@Override
	public ReservationDto getReservationById(UUID idReservation) {
		log.info("Fetching Reservation by id = {}", idReservation);
		return reservationRepository.findById(idReservation).get();
	}

	@Override
	public List<NotificationDto> notification() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAllReservations(List<ReservationDto> reservations) {
		log.info("add list reservations {}", reservations);
		reservationRepository.saveAll(reservations);
	}

}
