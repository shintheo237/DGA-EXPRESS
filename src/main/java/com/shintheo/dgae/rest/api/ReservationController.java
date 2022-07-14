package com.shintheo.dgae.rest.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shintheo.dgae.domaine.ReservationDto;
import com.shintheo.dgae.service.impl.ReservationServiceimpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "ReservationDto")
public class ReservationController {
	@Autowired
	private ReservationServiceimpl reservationServiceimpl;

	@RequestMapping(method = RequestMethod.POST, value = "/addReservation")
	@PostAuthorize("hasRole('ROLE_CLIENT')")
	@Operation(summary = "create a new Reservation")
	public void addReservation(@RequestBody ReservationDto reservationDto) {
		reservationServiceimpl.addReservation(reservationDto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/reservations")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "fetching all reservations")
	public List<ReservationDto> getAllReservation() {
		return reservationServiceimpl.getAllReservation();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/reservations/{id}")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Fetching reservation by id")
	public ReservationDto getReservationById(@PathVariable UUID id) {
		return reservationServiceimpl.getReservationById(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/reservations")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Get all reservation of particular user id")
	public List<ReservationDto> getReservationbyUser(@PathVariable UUID userId) {
		return reservationServiceimpl.getReservationByUser(userId);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/update/reseravtion")
	@Secured({ "ROLE_CLIENT" })
	@Operation(summary = "Update Reservation")
	public void updateReservation(@RequestBody ReservationDto reservationDto) {
		reservationServiceimpl.addReservation(reservationDto);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}/reservations")
	@Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Delete particular reservation")
	public void deleteReservation(@PathVariable UUID id) {
		reservationServiceimpl.deleteReservation(id);
	}

}
