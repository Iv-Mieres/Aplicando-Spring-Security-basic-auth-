package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.PublicationDTO;
import com.encuentro_musical.anuncios.model.Publication;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;

public interface IPublicationService {
	
	public void savePublication(HttpSession session, Publication publication) throws BadRequestException;
	
	public List<PublicationDTO> getAllBandOrMusicianPublications(HttpSession session);

	public void deletePublication(HttpSession session, Long idPublication) throws BadRequestException;
	
	// FILTROS DE BUSQUEDA
	
	public List<PublicationDTO> getFilterForGeneroMusical(HttpSession sesion, String filter);

	public List<PublicationDTO> getFilterForFechaPublicacion(HttpSession sesion, LocalDate fecha);
	
	public List<PublicationDTO> getFilterForProvincia(HttpSession session, String provincia) throws Exception;
	
	public List<PublicationDTO> getFilterForInstrumento(HttpSession session, String instrumento);

	void updatePubication(HttpSession session, Long idPublication, Publication publication) throws BadRequestException;



}
