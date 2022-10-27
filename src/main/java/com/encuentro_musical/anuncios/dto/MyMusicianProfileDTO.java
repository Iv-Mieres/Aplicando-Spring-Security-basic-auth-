package com.encuentro_musical.anuncios.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyMusicianProfileDTO {

	private String username;
	private String email;
	private String nombre;
	private String apellido;
	private String provincia;
	private String localidad;
	private String instrumento;
	private List<PublicationMDTO> listPublicationsMusician;

	public MyMusicianProfileDTO(String username, String email, String nombre, String apellido, String provincia,
			String localidad, String instrumento, List<PublicationMDTO> listPublicationsMusician) {
		this.username = username;
		this.email = email;
		this.nombre = nombre;
		this.apellido = apellido;
		this.provincia = provincia;
		this.localidad = localidad;
		this.instrumento = instrumento;
		this.listPublicationsMusician = listPublicationsMusician;
	}

	public MyMusicianProfileDTO() {
	}

}
