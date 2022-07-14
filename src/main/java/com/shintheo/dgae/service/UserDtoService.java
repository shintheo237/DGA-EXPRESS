package com.shintheo.dgae.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.shintheo.dgae.domaine.RoleDto;
import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.exceptions.DuplicatedValueException;
import com.shintheo.dgae.exceptions.NotFoundException;

public interface UserDtoService {

	List<UserDto> getAllUsers();

	UserDto loadUserByEmail(String email) throws NotFoundException;

	UserDto getUser(UUID id);

	void saveRole(RoleDto roleDto);

	void addRoleToUser(String email, String roleName);

	void deleteUser(UUID userIs);

	void addUser(UserDto userDto) throws DuplicatedValueException;

	void updateUser(UserDto userDto, Principal principal);

	void updatePassword(String oldPassword, String newPassword, String userEmail);
}
