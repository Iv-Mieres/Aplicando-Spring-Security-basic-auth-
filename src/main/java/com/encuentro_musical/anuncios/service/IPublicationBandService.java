package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.PaginadoDTO;
import com.encuentro_musical.anuncios.dto.PublicationBDTO;
import com.encuentro_musical.anuncios.model.BandPublication;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;

public interface IPublicationBandService {
	
	public void savePublication(HttpSession session, BandPublication bandPublication) throws BadRequestException;
	
	public PaginadoDTO getAllPublications(int pageNumber, int pageSize);

	public void deleteBandPublication(HttpSession session, Long idPublication) throws Exception;
	
	public List<PublicationBDTO> getPublicationByGeneroMusical(String generoMusical);
	
	public List<PublicationBDTO> getPublicationByFechaPublicacion(LocalDate fechaPublicacion);
	
	public List<PublicationBDTO> getPublicationByProvincia(String provincia);

}
