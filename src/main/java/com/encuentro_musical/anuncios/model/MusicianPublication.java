package com.encuentro_musical.anuncios.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MusicianPublication extends Publication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idMusicianPublication;
	
	@ManyToOne
	@JoinColumn(name = "id_Musician")
	@JsonIgnoreProperties(value = "listPublicationsMusician")
	private UserMusician userMusician;

	public MusicianPublication() {
	}

	public MusicianPublication(String linkRedSocial, String generoMusical, String titulo, LocalDate fechaPublicacion,
			String descripcion, UserMusician userMusician) {
		super(linkRedSocial, generoMusical, titulo, fechaPublicacion, descripcion);
		this.userMusician = userMusician;
	}
	
	
	

}
