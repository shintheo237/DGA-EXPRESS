package com.shintheo.dgae.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.shintheo.dgae.common.Status;
import com.shintheo.dgae.domaine.UserDto;

public interface UserRepository extends CrudRepository<UserDto, UUID> {
	Optional<UserDto> findByEmail(String email);
	List<UserDto> findByStatus(Status status);
}
