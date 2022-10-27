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
import com.encuentro_musical.anuncios.dto.MyBandProfileDTO;
import com.encuentro_musical.anuncios.dto.RegisterBDTO;
import com.encuentro_musical.anuncios.enums.Role;
import com.encuentro_musical.anuncios.model.BandPublication;
import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.repository.IBandPublicationRepository;
import com.encuentro_musical.anuncios.repository.IUserBandRepository;

@Service
public class UserBandService implements IUserBandService {

	@Autowired
	private IUserBandRepository userBandRepository;
	
	@Autowired
	private IBandPublicationRepository bandPublicationRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	// ====== REGISTRAR UN USUARIO BANDA =========
	
	@Override
	public UserBand saveUserBand(RegisterBDTO registerBand) throws Exception {

		var bandUserSet = modelMapper.map(registerBand, UserBand.class);
		bandUserSet.setRole(Role.BANDA);
		bandUserSet.setEliminado("FALSE");

		if (registerBand.getPassword().equals(registerBand.getRepeatPassword())) {
			bandUserSet.setPassword(passwordEncoder.encode(registerBand.getPassword()));
		} else {
			throw new Exception("Debe ingresar el mismo password en ambos campos");
		}
		if (userBandRepository.existsByEmail(registerBand.getEmail())) {
			throw new Exception("El email ya se encuentra registrado. Por favor ingrese un nuevo email!");
		}
		return userBandRepository.save(bandUserSet);
	}

	// ====== MOSTRAR PERFIL(DATOS) DEL USUARIO BANDA LOGUEADO =========

		@Override
		public MyBandProfileDTO myProfile(HttpSession session) {
			var userBand = (UserBand) session.getAttribute("usersession");

			var userBandBD = userBandRepository.findById(userBand.getIdBand()).orElseThrow();
			var addListBandPublication = new ArrayList<PublicationMDTO>();
			var bandProfileDTO = modelMapper.map(userBandBD, MyBandProfileDTO.class);
			List<BandPublication> bandPublications = userBandBD.getListPublicationsBand();

			for (BandPublication bandPublication : bandPublications) {
				if (Objects.isNull(bandPublication)) {
					Hibernate.initialize(userBandBD.getListPublicationsBand());
				} else {
					var bandPublicationDTO = modelMapper.map(userBandBD, PublicationMDTO.class);
					addListBandPublication.add(bandPublicationDTO);
				}
			}
			bandProfileDTO.setListPublicationsBand(addListBandPublication);
			return bandProfileDTO;
		}

		// ====== EDITAR USUARIO BANDA =========

		@Override
		public void updateBand(HttpSession session, UserBand userBand) {
			var updateUserBand = (UserBand) session.getAttribute("usersession");

			Long idBand = updateUserBand.getIdBand();
			var bandPublications = updateUserBand.getListPublicationsBand();

			updateUserBand = userBand;
			updateUserBand.setRole(Role.BANDA);
			updateUserBand.setIdBand(idBand);;
			updateUserBand.setPassword(passwordEncoder.encode(userBand.getPassword()));
			updateUserBand.setListPublicationsBand(bandPublications);
			updateUserBand.setEliminado("FALSE");

			userBandRepository.save(updateUserBand);
		}

		// ====== EDITAR PUBLICACIÓN DE UN USUARIO BANDA =========


		@Override
		public void updateBandPublication(HttpSession session, Long idBandPublication,
				BandPublication bandPublication) throws Exception {

			var bandSession = (UserBand) session.getAttribute("usersession");
			var bandPublicationBD = bandPublicationRepository.findById(idBandPublication)
					.orElseThrow(() -> new Exception("Id incorrecto. Ingrese un id válido"));
			
			if(!bandPublicationBD.getUserBand().getIdBand().equals(bandSession.getIdBand())) {
				throw new Exception("Id incorrecto. Ingrese un id válido");
			}else {	
				bandPublicationBD = bandPublication;
				bandPublicationBD.setUserBand(bandSession);
				bandPublicationBD.setIdBandPublication(idBandPublication);		
			}
			bandPublicationRepository.save(bandPublicationBD);
		}

		// ====== ELIMINADO LÓGICO DE UNA BANDA =========

		@Override
		public void deleteBand(HttpSession session) {
			var bandSession = (UserBand) session.getAttribute("usersession");
			var bandBD = userBandRepository.findById(bandSession.getIdBand()).orElseThrow();

			bandBD.setEliminado("TRUE");
			bandBD.setIdBand(bandSession.getIdBand());
			userBandRepository.save(bandBD);
		}



}
