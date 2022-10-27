package com.encuentro_musical.anuncios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {

	private String username;
	private String password;


	public LoginUserDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public LoginUserDTO() {
	}

}
