package com.encuentro_musical.anuncios.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyBandProfileDTO {

	private String username;
	private String email;
	private String provincia;
	private String localidad;
	private String nombreBanda;
	private List<PublicationMDTO> listPublicationsBand;

	public MyBandProfileDTO(String username, String email, String provincia, String localidad, String nombreBanda,
			List<PublicationMDTO> listPublicationsBand) {
		this.username = username;
		this.email = email;
		this.provincia = provincia;
		this.localidad = localidad;
		this.nombreBanda = nombreBanda;
		this.listPublicationsBand = listPublicationsBand;
	}

	public MyBandProfileDTO() {
	}

}
