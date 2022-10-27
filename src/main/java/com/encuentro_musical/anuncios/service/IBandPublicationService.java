package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.PublicationBDTO;
import com.encuentro_musical.anuncios.model.BandPublication;

public interface IBandPublicationService {
	
	public void savePublication(HttpSession session, BandPublication bandPublication);
	
	public List<PublicationBDTO> getAllPublications();

	public void deleteBandPublication(HttpSession session, Long idPublication) throws Exception;
	
	public List<PublicationBDTO> getPublicationByGeneroMusical(String generoMusical);
	
	public List<PublicationBDTO> getPublicationByFechaPublicacion(LocalDate fechaPublicacion);
	
	public List<PublicationBDTO> getPublicationByProvincia(String provincia);

}
