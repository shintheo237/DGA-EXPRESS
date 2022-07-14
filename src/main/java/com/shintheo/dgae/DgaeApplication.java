package com.shintheo.dgae;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shintheo.dgae.domaine.RoleDto;
import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.property.FileStorageProperties;
import com.shintheo.dgae.service.UserDtoService;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties({FileStorageProperties.class})
public class DgaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DgaeApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserDtoService userDtoService) {
		return arg -> {
			
			userDtoService.saveRole(new RoleDto(3, "ROLE_ADMIN"));
			userDtoService.saveRole(new RoleDto(2, "ROLE_CLIENT"));
			userDtoService.saveRole(new RoleDto(1, "ROLE_EMPLOYEE"));
			Collection<RoleDto> roles = new ArrayList<>();
			roles.add(new RoleDto(3, "ROLE_ADMIN"));
		};
	};

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OpenAPI openApiConfig() {
		return new OpenAPI().info(apiInfo());
	}

	public Info apiInfo() {
		Info info = new Info();
		info.title("DGA-EXPRESS API").description("Package transfer and sales application.").version("V1.2.15");
		return info;
	}

}
