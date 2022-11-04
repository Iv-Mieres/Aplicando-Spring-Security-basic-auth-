package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.encuentro_musical.anuncios.dto.MusicianDTO;
import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.model.MusicianPublication;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.repository.IMusicianPublicationRepository;
import com.encuentro_musical.anuncios.repository.IUserMusicianRepository;

@Service
public class PublicationMusicianService implements IPublicationMusicianService {

	@Autowired
	private IMusicianPublicationRepository musicianPublicationRepository;
	
	@Autowired
	private IUserMusicianRepository userMusicianRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	// GUARDAR ANUNCIO DE UN MÚSICO - CHECK OK
	
	@Override
	public void savePublication(HttpSession session, MusicianPublication musicianPublication) throws BadRequestException {
		UserMusician musicianSession =(UserMusician) session.getAttribute("usersession");
		
		if(!musicianPublication.getFechaPublicacion().equals(LocalDate.now())) {
			throw new BadRequestException("La fecha ingresada debe ser igual a la fecha de hoy: " + LocalDate.now());
		}
		MusicianPublication musicianPublicationSave = musicianPublication;
		musicianPublicationSave.setUserMusician(musicianSession);
		musicianPublicationRepository.save(musicianPublication);
	}

	// MOSTRAR ANUNCIOS PAGINADOS DE MÚSICOS

	@Override
	public List<PublicationMDTO> getAllPublications() {
		int pageNumber = 0;
		int pageSize = 5;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<MusicianPublication> listPublicationsBD = musicianPublicationRepository.findAll(pageable);
		var listMusicianPublications = new ArrayList<PublicationMDTO>();
		
		for (MusicianPublication musicianPublication : listPublicationsBD) {
			PublicationMDTO publicationDTO = modelMapper.map(musicianPublication, PublicationMDTO.class);
			MusicianDTO musicianDTO = modelMapper.map(musicianPublication.getUserMusician(), MusicianDTO.class);
			publicationDTO.setMusicianDTO(musicianDTO);
			listMusicianPublications.add(publicationDTO);
		}

		
		return listMusicianPublications;
	}
	
	// ELIMINAR ANUNCIO

	@Override
	public void deleteMusicianPublication(HttpSession session, Long idMusicianPublication) throws BadRequestException {

		var userMusician = (UserMusician) session.getAttribute("usersession");
		var publicationBD = musicianPublicationRepository.findById(idMusicianPublication).orElse(null);
		
		if(!userMusician.getIdMusician().equals(publicationBD.getUserMusician().getIdMusician())) {
		   throw new BadRequestException("El id " + idMusicianPublication + " no existe. Ingrese un id válido");
		}
		musicianPublicationRepository.delete(publicationBD); ;
	}
	
	// ========== FILTRAR ANUNCIOS POR... ======================

	@Override
	public List<PublicationMDTO> getPublicationByGeneroMusical(String generoMusical) {
		var listMusicianPublication = new ArrayList<PublicationMDTO>();

		for (MusicianPublication musicianPublication : this.getAllMusicianPublications() ) {
			if (musicianPublication.getGeneroMusical().equalsIgnoreCase(generoMusical)) {
				PublicationMDTO publicationDTO = modelMapper.map(musicianPublication, PublicationMDTO.class);
				MusicianDTO userMusician = modelMapper.map(musicianPublication.getUserMusician(), MusicianDTO.class);
				publicationDTO.setMusicianDTO(userMusician);
				listMusicianPublication.add(publicationDTO);
			}
		}
		return listMusicianPublication;
	}

	@Override
	public List<PublicationMDTO> getPublicationByFechaPublicacion(LocalDate fechaPublicacion) {
		var listMusicianPublication = new ArrayList<PublicationMDTO>();

		for (MusicianPublication musicianPublication : this.getAllMusicianPublications()) {
			if (musicianPublication.getFechaPublicacion().equals(fechaPublicacion)) {
				PublicationMDTO publicationDTO = modelMapper.map(musicianPublication, PublicationMDTO.class);
				MusicianDTO userMusician = modelMapper.map(musicianPublication.getUserMusician(), MusicianDTO.class);
			    publicationDTO.setMusicianDTO(userMusician);
				listMusicianPublication.add(publicationDTO);
			}
		}
		return listMusicianPublication;
	
	}

	@Override
	public List<PublicationMDTO> getPublicationByProvincia(String provincia) {
		var listMusicianPublication = new ArrayList<PublicationMDTO>();

		for (UserMusician userMusician : this.getAllMusicians()) {
			if (userMusician.getProvincia().equalsIgnoreCase(provincia)) {
				
				for (MusicianPublication musicianPublication : userMusician.getListPublicationsMusician()) {
					if (!Objects.isNull(musicianPublication)) {
						PublicationMDTO publicationDTO = modelMapper.map(musicianPublication, PublicationMDTO.class);
						MusicianDTO musicianDTO = modelMapper.map(userMusician, MusicianDTO.class);
						publicationDTO.setMusicianDTO(musicianDTO);
						listMusicianPublication.add(publicationDTO);
					}

				}

			}
		}
		return listMusicianPublication;
	}

	@Override
	public List<PublicationMDTO> getPublicationByInstrumento(String instrumento) {
		var listMusicianPublication = new ArrayList<PublicationMDTO>();
		
		for (UserMusician userMusician : this.getAllMusicians() ) {
			if(userMusician.getInstrumento().equalsIgnoreCase(instrumento)) {
				
				for (MusicianPublication musicianPublication : userMusician.getListPublicationsMusician()) {
					if (!Objects.isNull(musicianPublication)) {
						PublicationMDTO publicationDTO = modelMapper.map(musicianPublication, PublicationMDTO.class);
						MusicianDTO musicianDTO = modelMapper.map(userMusician, MusicianDTO.class);
						publicationDTO.setMusicianDTO(musicianDTO);
						listMusicianPublication.add(publicationDTO);
					}

				}
			}
		}
		return listMusicianPublication;
	}
	
	public List<MusicianPublication> getAllMusicianPublications(){
		return musicianPublicationRepository.findAll();
	}
	
	public List<UserMusician> getAllMusicians(){
		return userMusicianRepository.findAll();
	}

}
