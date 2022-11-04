package com.encuentro_musical.anuncios.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;

import com.encuentro_musical.anuncios.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "band_users")
public class UserBand extends UserModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idBand;
	
	@NotNull(message = "No puede estar vacio")
	@Size(min=3, message = "Debe contener un minimo de 3 caracteres")
	private String nombreBanda;
	
	@OneToMany(mappedBy = "userBand")
	@JsonIgnoreProperties (value = "userBand")
	private List<BandPublication> listPublicationsBand;
	
	public UserBand() {
	}

	public UserBand(String userName, String password, String email, String localidad, String provincia,
			String eliminado, String repeatPassword, Role role, String repeatEmail, List<BandPublication> listPublicationsBand, String nombreBanda) {
		super(userName, password, email, localidad, provincia, eliminado, repeatPassword, role, repeatEmail);
		this.listPublicationsBand = listPublicationsBand;
		this.nombreBanda = nombreBanda;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (this.getEliminado().equals("TRUE")) {
			return false;
		}
		return true;
	}
	
	}

