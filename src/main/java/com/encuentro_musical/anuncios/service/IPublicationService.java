package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.PaginadoDTO;
import com.encuentro_musical.anuncios.dto.PublicationDTO;
import com.encuentro_musical.anuncios.model.Publication;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;

public interface IPublicationService {
	
	public void savePublication(HttpSession session, Publication publication) throws BadRequestException;
	
	public PaginadoDTO getAllBandOrMusicianPublications(HttpSession session, int pageNumber, int pageSize);

	public void deletePublication(HttpSession session, Long idPublication) throws BadRequestException;
	
	// FILTROS DE BUSQUEDA
	
	public List<PublicationDTO> getPublicationByGeneroMusical(HttpSession session, String generoMusical);
	
	public List<PublicationDTO> getPublicationByFechaPublicacion(HttpSession session, LocalDate fechaPublicacion);
	
	public List<PublicationDTO> getPublicationByProvincia(HttpSession session, String provincia);
	
	public List<PublicationDTO> getPublicationByInstrumento(HttpSession session, String instrumento);

}
