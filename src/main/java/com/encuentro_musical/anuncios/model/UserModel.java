package com.encuentro_musical.anuncios.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.userdetails.UserDetails;

import com.encuentro_musical.anuncios.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class UserModel implements Serializable, UserDetails{

	private static final long serialVersionUID = 1L;

	@NotNull(message = "No puede estar vacio")
	@Size(min = 4, max = 50, message = "Debe contener entre 4 y 50 caracteres")
	@Column(unique = true)
	private String userName;

	@NotNull(message = "No puede estar vacio")
	private String password;
	
	@NotNull(message = "No puede estar vacio")
	@Email(message = "Ingrese un Email válido")
	@Column(unique = true)
	private String email;

	@NotNull(message = "No puede estar vacio")
	@Size(min = 4, max = 50, message = "Debe contener entre 4 y 50 caracteres")
	private String provincia;

	@NotNull(message = "No puede estar vacio")
	@Size(min = 4, max = 50, message = "Debe contener entre 4 y 50 caracteres")
	private String localidad;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private String eliminado;

	public UserModel() {
	}

	public UserModel(String userName, String password, String email, String localidad, String provincia, String eliminado) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.localidad = localidad;
		this.provincia = provincia;
		this.eliminado = eliminado;
	}

}
