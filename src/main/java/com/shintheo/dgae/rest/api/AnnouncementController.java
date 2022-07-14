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

import com.shintheo.dgae.domaine.AnnouncementDto;
import com.shintheo.dgae.service.impl.AnnouncementServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "AnnouncementDto")
public class AnnouncementController {

	@Autowired
	private AnnouncementServiceImpl announcementServiceImpl;

	@RequestMapping(method = RequestMethod.GET, value = "/announcements")
	@Operation(summary = "Fetching validated Announcements from DGA-Express")
	public List<AnnouncementDto> getAllValidAnnoucements() {
		return announcementServiceImpl.getAllValidAnnouncements();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/dashboard/announcements")
	@Secured({ "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Display all Announcements on admin dashboard")
	public List<AnnouncementDto> getAllAnnoucements() {
		return announcementServiceImpl.getAllAnnouncements();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/dashboard/validation/{id}")
	@Secured({ "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "turn on to validated or unvalidated  ")
	public void validateAnnouncement(@PathVariable UUID id) {
		announcementServiceImpl.changeValidation(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/announcement/{id}/users")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Get a specific announcement by id")
	public AnnouncementDto getAnnoucement(@PathVariable UUID id) {
		return announcementServiceImpl.getAnnoucement(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/{id}/announcements")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Get annoucement by user Id")
	public List<AnnouncementDto> getAnnouncementByUser(@PathVariable UUID id) {
		return announcementServiceImpl.getAnnouncementsByUser(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/createAnnouncement")
	@PostAuthorize("hasRole('ROLE_CLIENT')")
	@Operation(summary = "Post an annoucemment")
	public void addAnnoucement(@RequestBody AnnouncementDto announcementDto) {
		announcementServiceImpl.addAnnouncement(announcementDto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/search/{departuretown}/{destinationtown}/announcements")
	@Operation(summary = "Search Announcement by date and town")
	public List<AnnouncementDto> searchAnnouncement(@PathVariable String departuretown,
			@PathVariable String destinationtown) {
		return announcementServiceImpl.searchAnnouncements(departuretown, destinationtown);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/update/announcement")
	@PostAuthorize("hasRole('ROLE_CLIENT')")
	@Operation(summary = "Update an announcement")
	public void updateAnnoucement(@RequestBody AnnouncementDto announcementDto) {
		announcementServiceImpl.updateAnnoucement(announcementDto);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}/announcements")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Delete anouncement by Id")
	public void deleteAnnouncement(@PathVariable UUID id) {
		announcementServiceImpl.deleteAnnoucement(id);
	}

}