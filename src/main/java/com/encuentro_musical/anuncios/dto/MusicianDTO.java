package com.encuentro_musical.anuncios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicianDTO {

	private String email;
	private String provincia;
	private String localidad;
	private String nombre;
	private String instrumento;

	public MusicianDTO(String email, String provincia, String localidad, String nombre, String instrumento) {
		this.email = email;
		this.provincia = provincia;
		this.localidad = localidad;
		this.nombre = nombre;
		this.instrumento = instrumento;
	}

	public MusicianDTO() {
	}

}
