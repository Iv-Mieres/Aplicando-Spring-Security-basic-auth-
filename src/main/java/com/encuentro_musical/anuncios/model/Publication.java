package com.encuentro_musical.anuncios.model;

import java.time.LocalDate;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DiscriminatorFormula;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "anuncios")
public class Publication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idAnuncio;
	
	@NotNull(message = "No puede estar vacio")
	@Size(min = 4, max = 50, message = "Debe contener entre 4 y 50 caracteres")
	private String titulo;

	@NotNull(message = "No puede estar vacio")
	private LocalDate fechaPublicacion;
	
	private String linkRedSocial;
	
	@NotNull(message = "No puede estar vacio")
	@Size(min = 4, max = 50, message = "Debe contener entre 4 y 50 caracteres")
	private String generoMusical;
	
	private boolean remunerado;
	
	@ManyToOne
	@JoinColumn(name = "idBand")
	@JsonIgnoreProperties (value = "listPublicationsBand")
	private UserBand userBand;
	
	@ManyToOne
	@JoinColumn(name = "idMusician")
	@JsonIgnoreProperties(value = "listPublicationsMusician")
	private UserMusician userMusician;
	
	@Lob
	@Size(min = 20, max = 500, message = "Debe contener entre 20 y 500 caracteres")
	private String descripcion;

	public Publication() {
	}

	public Publication(String linkRedSocial, String generoMusical,	String titulo, LocalDate fechaPublicacion, 
				String descripcion, UserBand userBand, UserMusician userMusician) {
		this.titulo = titulo;	
		this.fechaPublicacion = fechaPublicacion;
		this.linkRedSocial = linkRedSocial;
		this.generoMusical = generoMusical;
		this.remunerado = false;
		this.descripcion = descripcion;
		this.userBand = userBand;
		this.userMusician = userMusician;
	}
	

}
