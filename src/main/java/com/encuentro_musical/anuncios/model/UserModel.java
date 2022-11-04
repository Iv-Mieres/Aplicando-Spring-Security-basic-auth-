package com.encuentro_musical.anuncios.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
	@Size(min = 6, message = "Debe contener un minimo de 6 caracteres")
	private String password;
	
	@NotNull(message = "No puede estar vacio")
	@Email(message = "Ingrese un Email válido")
	@NotBlank(message = "No puede estar vacio")
	private String email;
	
	@NotNull(message = "No puede estar vacio")
	@Email(message = "Ingrese un Email válido")
	@NotBlank(message = "No puede estar vacio")
	private String repeatEmail;

	@NotNull(message = "No puede estar vacio")
	@Size(min = 4, max = 50, message = "Debe contener entre 4 y 50 caracteres")
	private String provincia;

	@NotNull(message = "No puede estar vacio")
	@Size(min = 4, max = 50, message = "Debe contener entre 4 y 50 caracteres")
	private String localidad;
	
    private String repeatPassword;
	
	private String eliminado;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	public UserModel() {
	}

	public UserModel( String userName, String password, String email, String provincia, String localidad,
			String repeatPassword, String eliminado, Role role, String repeatEmail) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.provincia = provincia;
		this.localidad = localidad;
		this.repeatPassword = repeatPassword;
		this.eliminado = eliminado;
		this.role = role;
		this.repeatEmail = repeatEmail;
	}

	
}
