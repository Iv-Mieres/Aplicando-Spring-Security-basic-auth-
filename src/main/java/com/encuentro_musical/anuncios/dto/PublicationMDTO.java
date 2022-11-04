package com.encuentro_musical.anuncios.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicationMDTO {

	private String titulo;
	private LocalDate fechaPublicacion;
	private String linkRedSocial;
	private String generoMusical;
	private boolean remunerado;
	private String descripcion;
	private MusicianDTO musicianDTO;
	

	public PublicationMDTO(String titulo, LocalDate fechaPublicacion, String linkRedSocial, String generoMusical,
			boolean remunerado, String descripcion, MusicianDTO musicianDTO) {
		this.titulo = titulo;
		this.fechaPublicacion = fechaPublicacion;
		this.linkRedSocial = linkRedSocial;
		this.generoMusical = generoMusical;
		this.remunerado = remunerado;
		this.descripcion = descripcion;
		this.musicianDTO = musicianDTO;
	}

	public PublicationMDTO() {
	}

}
