package com.encuentro_musical.anuncios.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicationBDTO {

	private String titulo;
	private LocalDate fechaPublicacion;
	private String linkRedSocial;
	private String generoMusical;
	private boolean remunerado;
	private String descripcion;
	private BandDTO bandDTO;

	public PublicationBDTO(String titulo, LocalDate fechaPublicacion, String linkRedSocial, String generoMusical,
			boolean remunerado, String descripcion, BandDTO bandDTO) {
		this.titulo = titulo;
		this.fechaPublicacion = fechaPublicacion;
		this.linkRedSocial = linkRedSocial;
		this.generoMusical = generoMusical;
		this.remunerado = remunerado;
		this.descripcion = descripcion;
		this.bandDTO = bandDTO;
	}

	public PublicationBDTO() {
	}

}
