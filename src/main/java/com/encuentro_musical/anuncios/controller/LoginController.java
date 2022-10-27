package com.encuentro_musical.anuncios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro_musical.anuncios.dto.LoginUserDTO;

@RestController
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/usuarios/login")
	public ResponseEntity<String> authenticateUser(@RequestBody LoginUserDTO loginDTO) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authenticate);
		return ResponseEntity.status(HttpStatus.OK).body("Login: Ha iniciado sesion correctamente!");
	}
}
