package com.encuentro_musical.anuncios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterBDTO {

	private String userName;
	private String password;
	private String repeatPassword;
	private String email;
	private String provincia;
	private String localidad;
	private String nombreBanda;

	public RegisterBDTO(String userName, String password, String repeatPassword, String email, String provincia,
			String localidad, String nombreBanda) {
		this.userName = userName;
		this.password = password;
		this.repeatPassword = repeatPassword;
		this.email = email;
		this.provincia = provincia;
		this.localidad = localidad;
		this.nombreBanda = nombreBanda;
	}

	public RegisterBDTO() {
	}

}
