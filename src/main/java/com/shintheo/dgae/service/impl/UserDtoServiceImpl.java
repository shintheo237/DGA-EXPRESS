package com.shintheo.dgae.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shintheo.dgae.common.ErrorMessage;
import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.AnnouncementDto;
import com.shintheo.dgae.domaine.NotificationDto;
import com.shintheo.dgae.domaine.ReservationDto;
import com.shintheo.dgae.domaine.RoleDto;
import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.exceptions.DuplicatedValueException;
import com.shintheo.dgae.exceptions.EmptyValueException;
import com.shintheo.dgae.exceptions.FileStorageException;
import com.shintheo.dgae.exceptions.NotFoundException;
import com.shintheo.dgae.persistence.NotificationRepository;
import com.shintheo.dgae.persistence.RoleDtoRepository;
import com.shintheo.dgae.persistence.UserRepository;
import com.shintheo.dgae.property.FileStorageProperties;
import com.shintheo.dgae.rest.api.UserController;
import com.shintheo.dgae.service.UserDtoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDtoServiceImpl implements UserDtoService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleDtoRepository roleDtoRepository;
	@Autowired
	private AnnouncementServiceImpl announcementServiceImpl;
	@Autowired
	private ReservationServiceimpl reservationServiceimpl;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private Path fileStorageLocation;
	
//	@Autowired
//    public UserDtoServiceImpl(FileStorageProperties fileStorageProperties) {
//        this.fileStorageLocation = Paths.get(this.PROFILEDIRECTORY)
//                .toAbsolutePath().normalize();
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//        }
//    }
	

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Fetching All users");
		List<UserDto> userDtos = userRepository.findByStatus(Status.ENABLED);
		if (userDtos.isEmpty())
			throw new EmptyValueException(ErrorMessage.EMPTYVALUEEXCEPTION);
		return userDtos;
	}

	@Override
	public UserDto loadUserByEmail(String email) {
		log.info("Fetching User = {}",email);
		Optional<UserDto> userDto = userRepository.findByEmail(email);
		if (!userDto.isPresent())
			throw new NotFoundException(ErrorMessage.NOTFOUNDEXCEPTION);
		return userDto.get();
	}

	@Override
	public UserDto getUser(UUID id) {
		log.info("Fetching User By Id: {}", id);
		return userRepository.findById(id).get();
	}

	@Override
	public void addUser(UserDto newUserDto) {
		log.info("Adding User {}", newUserDto);
			Optional<UserDto> userDto = userRepository.findByEmail(newUserDto.getEmail());
			if (userDto.isPresent()) {
				throw new NotFoundException("Oups!! This email already has account.");
			}else {
				newUserDto.setStatus(Status.ENABLED);
				newUserDto.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
				if (newUserDto.getPseudo().equals("DGA-EMPLOYEE")) {
					userRepository.save(newUserDto);
					this.addRoleToUser(newUserDto.getEmail(), "ROLE_EMPLOYEE");
				} else {
					userRepository.save(newUserDto);
					this.addRoleToUser(newUserDto.getEmail(), "ROLE_CLIENT");
				}
			}
	}

	@Override
	public void updateUser(UserDto userUpdate, Principal principal) {
		log.info("Updating User {}", userUpdate);
		UserDto user = this.loadUserByEmail(principal.getName());
		user.setFirstName(userUpdate.getFirstName());
		user.setLastName(userUpdate.getLastName());
		user.setPseudo(userUpdate.getPseudo());
		user.setEmail(userUpdate.getEmail());
		userRepository.save(user);
	
		}

	@Override
	public void saveRole(RoleDto roleDto) {
		log.info("Adding Role {}", roleDto);
		roleDtoRepository.save(roleDto);
	}

	@Override
	public void addRoleToUser(String email, String roleName) {
		log.info("Adding Role {} To User {}", roleName, email);
		UserDto userDto = userRepository.findByEmail(email).get();
		RoleDto roleDto = roleDtoRepository.findByName(roleName);
		userDto.getRoleDtos().add(roleDto);
		userRepository.save(userDto);
	}

	@Override
	public void deleteUser(UUID userId) {
		log.info("Delete user by Id = {}", userId);
		UserDto userDto = userRepository.findById(userId).get();
		userDto.setStatus(Status.DISABLED);
		List<AnnouncementDto> announcementDtos = announcementServiceImpl.getAnnouncementsByUser(userId);
		announcementDtos.forEach(x -> x.setStatus(Status.DISABLED));
		announcementServiceImpl.addAllAnnouncements(announcementDtos);
		List<ReservationDto> reservationDtos = reservationServiceimpl.getReservationByUser(userId);
		reservationDtos.forEach(x -> x.setStatus(Status.DISABLED));
		reservationServiceimpl.addAllReservations(reservationDtos);
		List<NotificationDto> notificationDtos = notificationRepository.findByUserDtoId(userId);
		notificationDtos.forEach(x -> x.setStatus(Status.DISABLED));
		notificationRepository.saveAll(notificationDtos);
		userRepository.save(userDto);
	} 
	
	public void storeProfile(MultipartFile[] file, Principal principal) throws IOException {
		fileStorageLocation = Paths.get(UserController.PROFILEDIRECTORY).toAbsolutePath().normalize();
		Files.createDirectories(fileStorageLocation);
		UserDto user = this.loadUserByEmail(principal.getName());
		for (int i = 0; i < 1; i++) {
			Path fileNameAndPath = Paths.get(UserController.PROFILEDIRECTORY, user.getId() + "");
			Files.write(fileNameAndPath, file[i].getBytes());
		}
		user.setProfileimgage(UserController.PROFILEDIRECTORY);
		this.addUser(user);
	}
	
	private boolean checkIfValidOldPassword(final UserDto user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

	@Override
	public void updatePassword(String oldPassword, String newPassword, String userEmail) {
		UserDto currentUser = this.loadUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		if (!this.checkIfValidOldPassword(currentUser, oldPassword)) {
	        throw new RuntimeException("Please make sure you have entered the correct password ");
	    }else {
			currentUser.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(currentUser);
		}
	}

//	public void saveProfileImage(MultipartFile[] file, UUID userId) throws Throwable {
//		for (int i = 0; i < 1; i++) {
//			Path fileNameAndPath = Paths.get(UserController.PROFILEDIRECTORY, userId + "");
//			Files.write(fileNameAndPath, file[i].getBytes());
//		}
//	}
	
//	try {
//		if (fileName.contains("..")) {
//			throw new NotFoundException("Sorry! Filename contains invalid path sequence "+fileName);
//		}
//		Path targetLocation = this.fileStorageLocation.resolve(fileName);
//		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//		return fileName;
//	} catch (IOException e) {
//		throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
//	}
}
