package com.shintheo.dgae.rest.api;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shintheo.dgae.common.JWTutil;
import com.shintheo.dgae.domaine.UploadFileResponse;
import com.shintheo.dgae.domaine.UserDto;
import com.shintheo.dgae.service.impl.UserDtoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "UserDto")
public class UserController {
	
	public static final String PROFILEDIRECTORY = System.getProperty("user.dir")
			+ "/src/main/resources/static/profileImages/";
	
//	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserDtoServiceImpl userDtoServiceImpl;

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	@Secured({ "ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_CLIENT" })
	@Operation(summary = "Get all users of DGA-Express")
	public List<UserDto> getAllUsers() {
		return userDtoServiceImpl.getAllUsers();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/profile")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Get current User Profile")
	public UserDto profile(Principal principal) {
		return userDtoServiceImpl.loadUserByEmail(principal.getName());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{email}/users")
	@Secured({ "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Search user by email")
	public UserDto loadUserByEmail(@PathVariable String email) {
		return userDtoServiceImpl.loadUserByEmail(email);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "get a specific user in the DGA-Express system")
	public UserDto getUserById(@PathVariable UUID id) {
		return userDtoServiceImpl.getUser(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signup")
	@Operation(summary = "Register a new user in the systhem DGA-Express")
	public void addUser(@RequestBody UserDto userDto) throws Exception {
		userDtoServiceImpl.addUser(userDto);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/profile/image")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Set User Profile image")
	public void uploadProfileImage(@RequestParam("file") MultipartFile[] file, Principal principal) throws IOException {
		userDtoServiceImpl.storeProfile(file, principal);

//		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
//				.path(fileName).toUriString();

//		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/update/user")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Update a specific user in the DGA-Express system")
	public void updateUser(@RequestBody UserDto userDto, Principal principal) {
		userDtoServiceImpl.updateUser(userDto, principal);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/user/update/{oldPassword}/{newPassword}/password")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Update user password")
	public void updatePassword(@PathVariable String oldPassword, @PathVariable String newPassword, Principal principal) {
		userDtoServiceImpl.updatePassword(oldPassword, newPassword, principal.getName()); 
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/user/{id}/users")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Delete one User")
	public void deleteUser(@PathVariable UUID userDtoId) {
		userDtoServiceImpl.deleteUser(userDtoId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/refreshToken")
	@Secured({ "ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	@Operation(summary = "Refresh a token for user")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String auhtToken = request.getHeader(JWTutil.AUTH_HEADER);
		if (auhtToken != null && auhtToken.startsWith(JWTutil.PREFIX)) {
			try {
				String jwt = auhtToken.substring(JWTutil.PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(JWTutil.SECRET);
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
				String useremail = decodedJWT.getSubject();
				UserDto userDto = userDtoServiceImpl.loadUserByEmail(useremail);
				String jwtAccessToken = JWT.create().withSubject(userDto.getEmail())
						.withExpiresAt(new Date(System.currentTimeMillis() + JWTutil.EXPIRE_ACCESS_TOKEN))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles",
								userDto.getRoleDtos().stream().map(r -> r.getName()).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> idToken = new HashMap<>();
				idToken.put("access-token", jwtAccessToken);
				idToken.put("refresh-token", jwt);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new RuntimeException("Refresh token required");
		}
	}
}
