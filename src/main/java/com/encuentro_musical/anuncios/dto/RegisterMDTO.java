package com.encuentro_musical.anuncios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterMDTO {

	private String userName;
	private String password;
	private String repeatPassword;
	private String email;
	private String provincia;
	private String localidad;
	private String nombre;
	private String apellido;
	private String instrumento;

	public RegisterMDTO(String userName, String password, String repeatPassword, String email, String provincia,
			String localidad, String nombre, String apellido, String instrumento) {
		this.userName = userName;
		this.password = password;
		this.repeatPassword = repeatPassword;
		this.email = email;
		this.provincia = provincia;
		this.localidad = localidad;
		this.nombre = nombre;
		this.apellido = apellido;
		this.instrumento = instrumento;
	}

	public RegisterMDTO() {
	}

}
