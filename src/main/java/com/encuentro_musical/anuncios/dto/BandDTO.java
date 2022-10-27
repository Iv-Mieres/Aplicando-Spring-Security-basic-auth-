package com.encuentro_musical.anuncios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BandDTO {

	private String email;
	private String provincia;
	private String localidad;
	private String nombreBanda;

	public BandDTO(String email, String provincia, String localidad, String nombreBanda) {
		this.email = email;
		this.provincia = provincia;
		this.localidad = localidad;
		this.nombreBanda = nombreBanda;
	}

	public BandDTO() {
	}

}
