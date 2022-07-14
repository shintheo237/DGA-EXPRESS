package com.shintheo.dgae.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.service.UserDtoService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private UserDtoService userDtoService;

	public UserDetailsServiceImpl(UserDtoService userDtoService) {
		super();
		this.userDtoService = userDtoService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto userDto = userDtoService.loadUserByEmail(username);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		userDto.getRoleDtos().forEach(r -> {
			authorities.add(new SimpleGrantedAuthority(r.getName()));
		});
		return new User(userDto.getEmail(), userDto.getPassword(), authorities);
	}
}
