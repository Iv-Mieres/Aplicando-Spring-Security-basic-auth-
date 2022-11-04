package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.dto.MyMusicianProfileDTO;
import com.encuentro_musical.anuncios.enums.Role;
import com.encuentro_musical.anuncios.model.MusicianPublication;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.repository.IMusicianPublicationRepository;
import com.encuentro_musical.anuncios.repository.IUserBandRepository;
import com.encuentro_musical.anuncios.repository.IUserMusicianRepository;

@Service
public class UserMusicianService implements IUserMusicianService {

	@Autowired
	private IUserMusicianRepository userMusicianRepository;
	
	@Autowired
	private IUserBandRepository userBandRepository;

	@Autowired
	private IMusicianPublicationRepository musicianPublicationRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// ====== REGISTRAR UN USUARIO MÚSICO ========= CHECK OK

	@Override
	public UserMusician saveUserMusician(UserMusician registerMusician) throws BadRequestException {

		if(userMusicianRepository.existsByUserName(registerMusician.getUsername())
				|| userBandRepository.existsByUserName(registerMusician.getUsername())) {
			throw new BadRequestException("El username ingresado ya se encuentra registrado. "
												+ "Por favor ingrese un nuevo username!");	
		}
		if (userMusicianRepository.existsByEmail(registerMusician.getEmail())
				|| userBandRepository.existsByEmail(registerMusician.getEmail())) {
			throw new BadRequestException("El email ya se encuentra registrado. Por favor ingrese un nuevo email!");
		}
		if (!registerMusician.getPassword().equals(registerMusician.getRepeatPassword())) {
			throw new BadRequestException("Debe ingresar el mismo password en ambos campos");
		}
		if (!registerMusician.getEmail().equals(registerMusician.getRepeatEmail())) {
			throw new BadRequestException("Debe ingresar el mismo Email en ambos campos");
		}
		
		var musicianUserSet =registerMusician;
		musicianUserSet.setRole(Role.MUSICO);
		musicianUserSet.setEliminado("FALSE");
		musicianUserSet.setPassword(passwordEncoder.encode(registerMusician.getPassword()));
		musicianUserSet.setRepeatPassword("");
		
		return userMusicianRepository.save(musicianUserSet);
	}

	// ====== MOSTRAR PERFIL(DATOS) DEL USUARIO MÚSICO LOGUEADO ========= CHECK OK

	@Override
	public MyMusicianProfileDTO myMusicianProfile(HttpSession session) {
		
		var musicianSession = (UserMusician) session.getAttribute("usersession");
		var userMusicianBD = userMusicianRepository.findById(musicianSession.getIdMusician()).orElse(null);
		var musicianProfileDTO = modelMapper.map(userMusicianBD, MyMusicianProfileDTO.class);
		var addListMusicianPublication = new ArrayList<PublicationMDTO>();
			
		for (MusicianPublication musicianPublication : userMusicianBD.getListPublicationsMusician()) {
			if (Objects.isNull(musicianPublication)) {
				Hibernate.initialize(userMusicianBD.getListPublicationsMusician());
			}else { 
				var musicianPublicationDTO = modelMapper.map(musicianPublication, PublicationMDTO.class);
				addListMusicianPublication.add(musicianPublicationDTO);
			}
		}
		 musicianProfileDTO.setListPublicationsMusician(addListMusicianPublication); 
		return musicianProfileDTO;
	}

	// ====== EDITAR USUARIO MÚSICO ========= CHECK OK

	@Override
	public void updateMusician(HttpSession session, UserMusician userMusician) throws BadRequestException {
		var musicianSession = (UserMusician) session.getAttribute("usersession");
		var userMusicianBD = userMusicianRepository.findById(musicianSession.getIdMusician()).orElse(null);

		if (userMusicianRepository.existsByUserName(userMusician.getUserName())
				&& !userMusician.getUserName().equals(userMusicianBD.getUserName())
				|| userBandRepository.existsByUserName(userMusician.getUserName())) {

			throw new BadRequestException("El username ingresado ya existe. Por favor ingrese un nuevo username");
		}
		if (userMusicianRepository.existsByEmail(userMusician.getEmail()) && !userMusician.getEmail().equals(userMusicianBD.getEmail())
				|| userBandRepository.existsByEmail(userMusician.getEmail())) {

			throw new BadRequestException("El Email ingresado ya existe. Por favor ingrese un nuevo Email");
		}
		if (!userMusician.getPassword().equals(userMusician.getRepeatPassword())) {
			throw new BadRequestException("Debe ingresar el mismo password en ambos campos");
		}
		if (!userMusician.getEmail().equals(userMusician.getRepeatEmail())) {
			throw new BadRequestException("Debe ingresar el mismo Email en ambos campos");
		}
		userMusicianBD = userMusician;
		userMusicianBD.setRole(Role.MUSICO);
		userMusicianBD.setIdMusician(musicianSession.getIdMusician());
		userMusicianBD.setPassword(passwordEncoder.encode(userMusician.getPassword()));
		userMusicianBD.setRepeatPassword("");
		userMusicianBD.setEliminado("FALSE");

		userMusicianRepository.save(userMusicianBD);
	}

	// ====== EDITAR ANUNCIO DE MÚSICO ========= CHECK OK

	@Override
	public void updateMusicianPublication(HttpSession session, Long idMuicianPublication,
			MusicianPublication musicianPublication) throws BadRequestException {
		
		var musicianSession = (UserMusician) session.getAttribute("usersession");
		var musicianPublicationBD = musicianPublicationRepository.findById(idMuicianPublication)
				.orElseThrow(() -> new BadRequestException("Id incorrecto. Ingrese un id válido"));
		
		if(!musicianPublicationBD.getUserMusician().getIdMusician().equals(musicianSession.getIdMusician())) {
			throw new BadRequestException("Id incorrecto. Ingrese un id válido");
		}
		if(!musicianPublication.getFechaPublicacion().equals(LocalDate.now())) {
			throw new BadRequestException("La fecha ingresada debe ser igual a la fecha de hoy: " + LocalDate.now());
		}	
		musicianPublicationBD = musicianPublication;
		musicianPublicationBD.setUserMusician(musicianSession);
		musicianPublicationBD.setIdMusicianPublication(idMuicianPublication);		
		
		musicianPublicationRepository.save(musicianPublicationBD);
	}

	// ====== ELIMINADO LÓGICO DE UN MÚSICO =========

	@Override
	public void deleteMusician(HttpSession session) {
		var musicianSession = (UserMusician) session.getAttribute("usersession");
		var musicianBD = userMusicianRepository.findById(musicianSession.getIdMusician()).orElse(null);

		musicianBD.setEliminado("TRUE");
		userMusicianRepository.save(musicianBD);
		
		for (MusicianPublication musicianPublication : musicianBD.getListPublicationsMusician()) {
			musicianPublicationRepository.delete(musicianPublication);
		}
	}


}
