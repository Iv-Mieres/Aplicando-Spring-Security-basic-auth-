package com.encuentro_musical.anuncios.model;

import java.time.LocalDate;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Publication {
	
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
	
	@JsonIgnore
	private boolean eliminado;

	@Lob
	@Size(min = 20, max = 500, message = "Debe contener entre 20 y 500 caracteres")
	private String descripcion;

	public Publication() {
	}

	public Publication(String linkRedSocial, String generoMusical,	String titulo, LocalDate fechaPublicacion, 
				String descripcion) {
		this.titulo = titulo;	
		this.fechaPublicacion = fechaPublicacion;
		this.linkRedSocial = linkRedSocial;
		this.generoMusical = generoMusical;
		this.remunerado = false;
		this.eliminado = false;
		this.descripcion = descripcion;
	}
	

}
