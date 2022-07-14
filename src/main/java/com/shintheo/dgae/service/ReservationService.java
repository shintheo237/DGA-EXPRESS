package com.shintheo.dgae.service;

import java.util.List;
import java.util.UUID;

import com.shintheo.dgae.domaine.NotificationDto;
import com.shintheo.dgae.domaine.ReservationDto;
import com.shintheo.dgae.exceptions.EmptyValueException;
import com.shintheo.dgae.exceptions.NotFoundException;

public interface ReservationService {

	void addReservation(ReservationDto reservationDto);

	void deleteReservation(UUID idReservationDto);

	List<ReservationDto> getReservationByUser(UUID userId) throws NotFoundException;

	List<ReservationDto> getAllReservation() throws EmptyValueException;

	ReservationDto getReservationById(UUID idReservation);

	void addAllReservations(List<ReservationDto> reservations);

	List<NotificationDto> notification();
}
