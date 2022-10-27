package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.model.MusicianPublication;

public interface IMusicianPublicationService {
	
	public void savePublication(HttpSession session, MusicianPublication musicianPublication);
	
	public List<MusicianPublication> getAllPublications();
	
	public void deleteMusicianPublication(Long idMusicianPublication) throws Exception;
	
	public void editMusicianPublication(Long idMusicianPublication, MusicianPublication MusicianPublication);
	
	public List<PublicationMDTO> getPublicationByGeneroMusical(String generoMusical);
	
	public List<PublicationMDTO> getPublicationByFechaPublicacion(LocalDate fechaPublicacion);
	
	public List<PublicationMDTO> getPublicationByProvincia(String provincia);
	
	public List<PublicationMDTO> getPublicationByInstrumento(String instrumento);



}
