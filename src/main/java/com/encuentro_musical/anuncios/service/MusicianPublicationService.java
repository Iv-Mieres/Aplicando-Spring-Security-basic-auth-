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

import com.encuentro_musical.anuncios.dto.BandDTO;
import com.encuentro_musical.anuncios.dto.MusicianDTO;
import com.encuentro_musical.anuncios.dto.PublicationBDTO;
import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.model.BandPublication;
import com.encuentro_musical.anuncios.model.MusicianPublication;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.repository.IMusicianPublicationRepository;

@Service
public class MusicianPublicationService implements IMusicianPublicationService {

	@Autowired
	private IMusicianPublicationRepository musicianPublicationRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	// Guardar un anuncio
	
	@Override
	public void savePublication(HttpSession session, MusicianPublication musicianPublication) {
		UserMusician musicianSession =(UserMusician) session.getAttribute("usersession");
		MusicianPublication musicianPublicationSave = musicianPublication;
		musicianPublicationSave.setUserMusician(musicianSession);
		musicianPublicationRepository.save(musicianPublication);
	}

	// Mostrar anuncios de Musicos

	@Override
	public List<MusicianPublication> getAllPublications() {
		int pageNumber = 0;
		int pageSize = 5;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<MusicianPublication> listPublicationsBD = musicianPublicationRepository.findAll(pageable);
		List<MusicianPublication> listMusicianPublication = listPublicationsBD.getContent();
		
		return listMusicianPublication;
	}
	
	// eliminar anuncio de un músico

	@Override
	public void deleteMusicianPublication(Long idMusicianPublication) throws Exception {

		if (!musicianPublicationRepository.existsById(idMusicianPublication)) {
			throw new Exception("El id " + idMusicianPublication + " no existe. Ingrese un id válido");
		}
		MusicianPublication publicationBD = musicianPublicationRepository.findById(idMusicianPublication).orElseThrow();
		publicationBD.setEliminado(true);

		//this.savePublication(publicationBD);

	}

	// Editar anuncio de un músico
	
	@Override
	public void editMusicianPublication(Long idMusicianPublication, MusicianPublication musicianPublication) {
		var publicationBD = musicianPublicationRepository.findById(idMusicianPublication).orElseThrow();
		publicationBD = musicianPublication;

		// this.savePublication(publicationBD);
		
	}
	
	// ========== FILTRAR ANUNCIOS POR... ======================

	@Override
	public List<PublicationMDTO> getPublicationByGeneroMusical(String generoMusical) {
		var listMusicianPublication = new ArrayList<PublicationMDTO>();

		for (MusicianPublication musicianPublication : this.getAllMusicianPublications() ) {
			if (musicianPublication.getGeneroMusical().equals(generoMusical)) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationMDTO> getPublicationByProvincia(String provincia) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationMDTO> getPublicationByInstrumento(String instrumento) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<MusicianPublication> getAllMusicianPublications(){
		return musicianPublicationRepository.findAll();
	}

}
