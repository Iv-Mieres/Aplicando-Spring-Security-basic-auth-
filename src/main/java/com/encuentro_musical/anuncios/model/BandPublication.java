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
public class BandPublication extends Publication {
	
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE)
	private Long idBandPublication; 
	
	@ManyToOne
	@JoinColumn(name = "id_Band")
	@JsonIgnoreProperties (value = "listPublicationsBand")
	private UserBand userBand;

	public BandPublication() {

	}

	public BandPublication(String linkRedSocial, String generoMusical, String titulo, LocalDate fechaPublicacion,
			String descripcion, UserBand userBand) {
		super(linkRedSocial, generoMusical, titulo, fechaPublicacion, descripcion);
		this.userBand = userBand;
	}

	
	
}
