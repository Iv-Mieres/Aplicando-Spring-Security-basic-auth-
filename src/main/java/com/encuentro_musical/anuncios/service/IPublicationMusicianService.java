package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.model.MusicianPublication;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;

public interface IPublicationMusicianService {
	
	public void savePublication(HttpSession session, MusicianPublication musicianPublication) throws BadRequestException;
	
	public List<PublicationMDTO> getAllPublications();
	
	public void deleteMusicianPublication(HttpSession session, Long idMusicianPublication) throws Exception;
	
	public List<PublicationMDTO> getPublicationByGeneroMusical(String generoMusical);
	
	public List<PublicationMDTO> getPublicationByFechaPublicacion(LocalDate fechaPublicacion);
	
	public List<PublicationMDTO> getPublicationByProvincia(String provincia);
	
	public List<PublicationMDTO> getPublicationByInstrumento(String instrumento);



}
