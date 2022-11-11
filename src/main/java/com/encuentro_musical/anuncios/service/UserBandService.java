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

import com.encuentro_musical.anuncios.dto.MyBandProfileDTO;
import com.encuentro_musical.anuncios.dto.PublicationDTO;
import com.encuentro_musical.anuncios.enums.Role;
import com.encuentro_musical.anuncios.model.Publication;
import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.repository.IPublicationRepository;
import com.encuentro_musical.anuncios.repository.IUserBandRepository;
import com.encuentro_musical.anuncios.repository.IUserMusicianRepository;

@Service
public class UserBandService implements IUserBandService {

	@Autowired
	private IUserBandRepository userBandRepository;

	@Autowired
	private IPublicationRepository publicationRepository;

	@Autowired
	private IUserMusicianRepository userMusicianRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	// ====== REGISTRAR UN USUARIO BANDA =========

	@Override
	public UserBand saveUserBand(UserBand registerBand) throws BadRequestException {

		if (userBandRepository.existsByUserName(registerBand.getUserName())
				|| userMusicianRepository.existsByUserName(registerBand.getUserName())) {
			throw new BadRequestException(
					"El username ingresado ya se encuentra registrado. Por favor ingrese un nuevo username!");
		}
		if (userBandRepository.existsByEmail(registerBand.getEmail())
				|| userMusicianRepository.existsByEmail(registerBand.getEmail())) {
			throw new BadRequestException(
					"El Email ingresado ya se encuentra registrado. Por favor ingrese un nuevo Email!");
		}
		if (!registerBand.getEmail().equals(registerBand.getRepeatEmail())) {
			throw new BadRequestException("Debe ingresar el mismo Email en ambos campos");
		}
		if (!registerBand.getPassword().equals(registerBand.getRepeatPassword())) {
			throw new BadRequestException("Debe ingresar el mismo password en ambos campos");
		}
		var bandUserSet = registerBand;
		bandUserSet.setRole(Role.BANDA);
		bandUserSet.setEliminado("FALSE");
		bandUserSet.setPassword(passwordEncoder.encode(registerBand.getPassword()));
		bandUserSet.setRepeatPassword("");

		return userBandRepository.save(bandUserSet);
	}

	// ====== MOSTRAR PERFIL DE BANDA LOGUEADA =========

	@Override
	public MyBandProfileDTO myProfile(HttpSession session) {

		var bandSession = (UserBand) session.getAttribute("usersession");
		var userBandBD = userBandRepository.findById(bandSession.getIdBand()).orElse(null);
		var bandProfileDTO = modelMapper.map(userBandBD, MyBandProfileDTO.class);
		var addListBandPublication = new ArrayList<PublicationDTO>();

		for (var bandPublication : userBandBD.getListPublicationsBand()) {
			if (Objects.isNull(bandPublication)) {
				Hibernate.initialize(userBandBD.getListPublicationsBand());
			} else {
				var bandPublicationDTO = modelMapper.map(bandPublication, PublicationDTO.class);
				addListBandPublication.add(bandPublicationDTO);
			}
		}
		bandProfileDTO.setListPublicationsBand(addListBandPublication);
		return bandProfileDTO;
	}

	// ====== EDITAR USUARIO BANDA =========

	@Override
	public void updateBand(HttpSession session, UserBand userBand) throws BadRequestException {
		var bandSession = (UserBand) session.getAttribute("usersession");
		var userBandBD = userBandRepository.findById(bandSession.getIdBand()).orElse(null);
		var bandPublications = bandSession.getListPublicationsBand();

		if (userBandRepository.existsByUserName(userBand.getUserName())
				&& !userBand.getUserName().equals(userBandBD.getUserName())
				|| userMusicianRepository.existsByUserName(userBand.getUserName())) {
			throw new BadRequestException("El username ingresado ya existe. Por favor ingrese un nuevo username");
		}
		if (userBandRepository.existsByEmail(userBand.getEmail()) && !userBand.getEmail().equals(userBandBD.getEmail())
				|| userMusicianRepository.existsByEmail(userBand.getEmail())) {
			throw new BadRequestException("El Email ingresado ya existe. Por favor ingrese un nuevo Email");
		}
		if (!userBand.getEmail().equals(userBand.getRepeatEmail())) {
			throw new BadRequestException("Debe ingresar el mismo Email en ambos campos");
		}
		if (!userBand.getPassword().equals(userBand.getRepeatPassword())) {
			throw new BadRequestException("Debe ingresar el mismo password en ambos campos");
		}
		userBandBD = userBand;
		userBandBD.setRole(Role.BANDA);
		userBandBD.setIdBand(bandSession.getIdBand());
		userBandBD.setPassword(passwordEncoder.encode(userBand.getPassword()));
		userBandBD.setRepeatPassword("");
		bandSession.setListPublicationsBand(bandPublications);
		userBandBD.setEliminado("FALSE");

		userBandRepository.save(userBandBD);
	}

	// ====== EDITAR ANUNCIO DE UNA BANDA =========

	@Override
	public void updateBandPublication(HttpSession session, Long idBandPublication, Publication bandPublication)
			throws BadRequestException {

		var bandSession = (UserBand) session.getAttribute("usersession");
		var publicationBD = publicationRepository.findById(idBandPublication).orElseThrow(
				() -> new BadRequestException("El id " + idBandPublication + " no es correcto. Ingrese un id válido"));

		if (Objects.isNull(publicationBD.getUserBand())
				|| !publicationBD.getUserBand().getIdBand().equals(bandSession.getIdBand())) {
			throw new BadRequestException("El id " + idBandPublication + " no es correcto. Ingrese un id válido");
		}
		if (!bandPublication.getFechaPublicacion().equals(LocalDate.now())) {
			throw new BadRequestException("La fecha ingresada debe ser igual a la fecha de hoy: " + LocalDate.now());
		}
		publicationBD = bandPublication;
		publicationBD.setUserBand(bandSession);
		publicationBD.setIdAnuncio(idBandPublication);

		publicationRepository.save(publicationBD);
	}

	// ====== ELIMINADO LÓGICO DE USUARIO BANDA =========

	@Override
	public void deleteBand(HttpSession session) {
		var bandSession = (UserBand) session.getAttribute("usersession");
		var bandBD = userBandRepository.findById(bandSession.getIdBand()).orElse(null);

		bandBD.setEliminado("TRUE");
		userBandRepository.save(bandBD);

		// elimina todas las publicaciones del usuario banda eliminado
		for (var bandPublication : bandBD.getListPublicationsBand()) {
			publicationRepository.delete(bandPublication);
		}
	}

}
