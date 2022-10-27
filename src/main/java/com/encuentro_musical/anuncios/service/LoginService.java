package com.encuentro_musical.anuncios.service;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.repository.IUserBandRepository;
import com.encuentro_musical.anuncios.repository.IUserMusicianRepository;

@Service
public class LoginService implements UserDetailsService {

	@Autowired
	private IUserBandRepository bandRepository;

	@Autowired
	private IUserMusicianRepository musicianRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserBand userBand = bandRepository.findByuserName(username);
		UserMusician userMusician = musicianRepository.findByuserName(username);

		// ============ SI SE LOGUEA UNA BANDA ==========================

		if (userBand != null && userBand.getUserName().equals(username)) {
			var permisos = new ArrayList<GrantedAuthority>();
			var p = new SimpleGrantedAuthority("ROLE_" + userBand.getRole().toString());
			permisos.add(p);
			
			// Devuelve los datos de la session iniciada
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usersession", userBand);

			return new User(userBand.getUsername(), userBand.getPassword(), userBand.isEnabled(), true, true, true,
					permisos);
		}

		// ============== SI SE LOGUEA UN MÚSICO ========================

		if (userMusician != null && userMusician.getUserName().equals(username)) {
			var permisos = new ArrayList<GrantedAuthority>();
			var p = new SimpleGrantedAuthority("ROLE_" + userMusician.getRole().toString());
			permisos.add(p);

			// Devuelve los datos de la session iniciada
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usersession", userMusician);

			return new User(userMusician.getUsername(), userMusician.getPassword(), userMusician.isEnabled(), true,
					true, true, permisos);
		} else {
			throw new UsernameNotFoundException("El username ingresado no existe. Ingrese un username correcto!");
		}

	}

}
