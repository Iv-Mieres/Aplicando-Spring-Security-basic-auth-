package com.encuentro_musical.anuncios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.dto.MyMusicianProfileDTO;
import com.encuentro_musical.anuncios.dto.RegisterMDTO;
import com.encuentro_musical.anuncios.enums.Role;
import com.encuentro_musical.anuncios.model.MusicianPublication;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.repository.IMusicianPublicationRepository;
import com.encuentro_musical.anuncios.repository.IUserMusicianRepository;

@Service
public class UserMusicianService implements IUserMusicianService {

	@Autowired
	private IUserMusicianRepository userMusicianRepository;

	@Autowired
	private IMusicianPublicationRepository musicianPublicationRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// ====== REGISTRAR UN USUARIO MÚSICO =========

	@Override
	public UserMusician saveUserMusician(RegisterMDTO registerMusician) throws Exception {
		var musicianUserSet = modelMapper.map(registerMusician, UserMusician.class);
		musicianUserSet.setRole(Role.MUSICO);
		musicianUserSet.setEliminado("FALSE");

		if (registerMusician.getPassword().equals(registerMusician.getRepeatPassword())) {
			musicianUserSet.setPassword(passwordEncoder.encode(registerMusician.getPassword()));
		} else {
			throw new Exception("Debe ingresar el mismo password en ambos campos");
		}
		if (userMusicianRepository.existsByEmail(registerMusician.getEmail())) {
			throw new Exception("El email ya se encuentra registrado. Por favor ingrese un nuevo email!");
		}
		return userMusicianRepository.save(musicianUserSet);
	}

	// ====== MOSTRAR PERFIL(DATOS) DEL USUARIO MÚSICO LOGUEADO =========

	@Override
	public MyMusicianProfileDTO myProfile(HttpSession session) {
		var userMusician = (UserMusician) session.getAttribute("usersession");

		var userMusicianBD = userMusicianRepository.findById(userMusician.getIdMusician()).orElseThrow();
		var agregarListMusicianPublication = new ArrayList<PublicationMDTO>();
		var musicianProfileDTO = modelMapper.map(userMusicianBD, MyMusicianProfileDTO.class);
		List<MusicianPublication> musicianPublications = userMusicianBD.getListPublicationsMusician();

		for (MusicianPublication musicianPublication : musicianPublications) {
			if (Objects.isNull(musicianPublication)) {
				Hibernate.initialize(userMusicianBD.getListPublicationsMusician());
			} else {
				var musicianPublicationDTO = modelMapper.map(musicianPublication, PublicationMDTO.class);
				agregarListMusicianPublication.add(musicianPublicationDTO);
			}
		}
		musicianProfileDTO.setListPublicationsMusician(agregarListMusicianPublication);
		return musicianProfileDTO;
	}

	// ====== EDITAR USUARIO MÚSICO =========

	@Override
	public void updateMusician(HttpSession session, UserMusician userMusician) {
		var updateUserMusician = (UserMusician) session.getAttribute("usersession");

		Long idMusician = updateUserMusician.getIdMusician();
		var musicianPublications = updateUserMusician.getListPublicationsMusician();

		updateUserMusician = userMusician;
		updateUserMusician.setRole(Role.MUSICO);
		updateUserMusician.setIdMusician(idMusician);
		updateUserMusician.setPassword(passwordEncoder.encode(userMusician.getPassword()));
		updateUserMusician.setListPublicationsMusician(musicianPublications);
		updateUserMusician.setEliminado("FALSE");

		userMusicianRepository.save(updateUserMusician);
	}

	// ====== EDITAR PUBLICACIÓN DE UN USUARIO MÚSICO =========


	@Override
	public void updateMusicianPublication(HttpSession session, Long idMuicianPublication,
			MusicianPublication musicianPublication) throws Exception {

		var musicianSession = (UserMusician) session.getAttribute("usersession");
		var musicianPublicationBD = musicianPublicationRepository.findById(idMuicianPublication)
				.orElseThrow(() -> new Exception("Id incorrecto. Ingrese un id válido"));
		
		if(!musicianPublicationBD.getUserMusician().getIdMusician().equals(musicianSession.getIdMusician())) {
			throw new Exception("Id incorrecto. Ingrese un id válido");
		}else {	
			musicianPublicationBD = musicianPublication;
			musicianPublicationBD.setUserMusician(musicianSession);
			musicianPublicationBD.setIdMusicianPublication(idMuicianPublication);		
		}
		musicianPublicationRepository.save(musicianPublicationBD);
	}

	// ====== ELIMINADO LÓGICO DE UN MÚSICO =========

	@Override
	public void deleteMusician(HttpSession session) {
		var musicianSession = (UserMusician) session.getAttribute("usersession");
		var musicianBD = userMusicianRepository.findById(musicianSession.getIdMusician()).orElseThrow();

		musicianBD.setEliminado("TRUE");
		musicianBD.setIdMusician(musicianSession.getIdMusician());
		userMusicianRepository.save(musicianBD);
	}


}
