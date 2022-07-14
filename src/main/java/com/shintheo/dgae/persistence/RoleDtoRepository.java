package com.shintheo.dgae.persistence;

import org.springframework.data.repository.CrudRepository;

import com.shintheo.dgae.domaine.RoleDto;

public interface RoleDtoRepository extends CrudRepository<RoleDto, Long> {
	RoleDto findByName(String name);
}
