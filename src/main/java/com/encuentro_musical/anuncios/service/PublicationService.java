package com.encuentro_musical.anuncios.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.encuentro_musical.anuncios.dto.BandDTO;
import com.encuentro_musical.anuncios.dto.MusicianDTO;
import com.encuentro_musical.anuncios.dto.PublicationDTO;
import com.encuentro_musical.anuncios.model.Publication;
import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.model.UserModel;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.repository.IPublicationRepository;

@Service
public class PublicationService implements IPublicationService {

	@Autowired
	private IPublicationRepository publicationRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	// GUARDAR PUBLICACIONES 

	@Override
	public void savePublication(HttpSession session, Publication publication) throws BadRequestException {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		Publication savePublication;

		if (!publication.getFechaPublicacion().equals(LocalDate.now())) {
			throw new BadRequestException("La fecha ingresada debe ser igual a la fecha de hoy: " + LocalDate.now());
		}
		switch (userSession.getRole()) {

		case MUSICO: //La publicación se guarda si el role de userSession es MUSICO
			UserMusician userMusician = (UserMusician) session.getAttribute("usersession");
			savePublication = publication;
			savePublication.setUserMusician(userMusician);
			break;
		case BANDA://La publicación se guarda si el role de userSession es BANDA
			UserBand bandSession = (UserBand) session.getAttribute("usersession");
			savePublication = publication;
			savePublication.setUserBand(bandSession);
			break;
		default://No puede guardarse la publicacion si el role de userSession es otro 
			throw new BadRequestException("No tiene los permisos para crear una publicacion");
		}
		publicationRepository.save(savePublication);

	}

	// MUESTRA LAS PUBLICACIONES DE BANDAS O MUSICOS

	@Override
	public List<PublicationDTO> getAllBandOrMusicianPublications(HttpSession session) {
		UserModel userSession = (UserModel) session.getAttribute("usersession");	
		List<Publication> listPublicationsBD = publicationRepository.findAll();
	
		switch (userSession.getRole()) {
		
		//Muestra las publicaciones de músicos si el role userSession es MUSICO
		case MUSICO:
			var listPublicationDTO = new ArrayList<PublicationDTO>();
			for (var publicationBD : listPublicationsBD) {
				if ((!Objects.isNull(publicationBD.getUserBand()) && publicationBD.getUserBand().isEnabled())
						&& Objects.isNull(publicationBD.getUserMusician())) {
					var publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					var bandDTO = modelMapper.map(publicationBD.getUserBand(), BandDTO.class);
					publicationDTO.setBandDTO(bandDTO);
					listPublicationDTO.add(publicationDTO);
				}
			}
			return listPublicationDTO;
			
		//Muestra las publicaciones de bandas si el role userSession es BANDA
		case BANDA:
			var listPublicationMDTO = new ArrayList<PublicationDTO>();
			for (var publicationBD : listPublicationsBD) {
				if((!Objects.isNull(publicationBD.getUserMusician()) && publicationBD.getUserMusician().isEnabled()) 
					&& Objects.isNull(publicationBD.getUserBand())){
					var publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					var musicianDTO = modelMapper.map(publicationBD.getUserMusician(), MusicianDTO.class);
					publicationDTO.setMusicianDTO(musicianDTO);
					listPublicationMDTO.add(publicationDTO);
				}
			}
			return listPublicationMDTO;
		default:
			return null;
		}
	}
	
	// EDITAR ANUNCIO COMO USUARIO BANDA O MUSICO
	
	@Override
	public void updatePubication(HttpSession session, Long idPublication, Publication publication) throws BadRequestException{
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var publicationBD = publicationRepository.findById(idPublication)
				.orElseThrow(() -> new BadRequestException("El id " + idPublication + " ingresado es incorecto. Ingrese un id válido!"));
		
		switch(userSession.getRole()) {
		case MUSICO:
			UserMusician musicianSession = (UserMusician) session.getAttribute("usersession");
			if(Objects.isNull(publicationBD.getUserMusician()) 
					||  !publicationBD.getUserMusician().getEmail().equals(userSession.getEmail())) {
				throw new BadRequestException("El id " + idPublication + " ingresado es incorecto. Ingrese un id válido!");
			}
			publicationBD = publication;
			publicationBD.setIdAnuncio(idPublication);
			publicationBD.setUserMusician(musicianSession);
			break;
		case BANDA:
			UserBand bandSession = (UserBand) session.getAttribute("usersession");
			if( Objects.isNull(publicationBD.getUserBand())
					|| !publicationBD.getUserBand().getEmail().equals(userSession.getEmail())) {
				throw new BadRequestException("El id " + idPublication + " ingresado es incorecto. Ingrese un id válido!");
			}
			publicationBD = publication;
			publicationBD.setIdAnuncio(idPublication);
			publicationBD.setUserBand(bandSession);
			break;
			default: 
				throw new BadRequestException("No tiene los permisos necesarios para utilizar este recurso!");
		}
		publicationRepository.save(publicationBD);
	}
	

	// ELIMINAR ANUNCIO COMO USUARIO MUSICO, BANDA O ADMIN

	@Override
	public void deletePublication(HttpSession session, Long idPublication) throws BadRequestException {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var publicationBD = publicationRepository.findById(idPublication)
				.orElseThrow(() -> new BadRequestException("El id " + idPublication + " no es correcto. Ingrese un id válido"));
		
		switch (userSession.getRole()) {

		case MUSICO:
			if (Objects.isNull(publicationBD.getUserMusician()) || 
					!publicationBD.getUserMusician().getEmail().equals(userSession.getEmail())) {
				throw new BadRequestException("El id " + idPublication + " no es correcto. Ingrese un id válido"
						+ " o vuelva a loguearse si ha editado sus datos de perfil! ");
			}
			publicationRepository.delete(publicationBD);
			break;
		case BANDA: 
			if (Objects.isNull(publicationBD.getUserBand()) ||
					!publicationBD.getUserBand().getEmail().equals(userSession.getEmail())) {
				throw new BadRequestException("El id " + idPublication + " no es correcto. Ingrese un id válido"
						+ " o vuelva a loguearse si ha editado sus datos de perfil! ");
			}
			publicationRepository.delete(publicationBD);
			break;
		case ADMIN:
			publicationRepository.delete(publicationBD);
		default:
			throw new BadRequestException("No tiene los permisos necesarios para utilizar este recurso!");
		}

	}
	
	//============== FILTROS DE BUSQUEDA ==============
	
	// BUSCAR POR GENERO MUSICAL
	
	@Override
	public List<PublicationDTO> getFilterForGeneroMusical(HttpSession session, String generoMusical){
		var publicationList = this.getAllBandOrMusicianPublications(session);	 
		var addPublication = new ArrayList<PublicationDTO>();
		
		for (PublicationDTO publicationBD : publicationList) {
			if(publicationBD.getGeneroMusical().equalsIgnoreCase(generoMusical)) {
				addPublication.add(publicationBD);		
			}			
		}	
		return addPublication;	
	}
	
	// BUSCAR POR FECHA DE PUBLICACIÓN
	
	@Override
	public List<PublicationDTO> getFilterForFechaPublicacion(HttpSession session, LocalDate fecha){
		var publicationList = this.getAllBandOrMusicianPublications(session);	 
		var addPublication = new ArrayList<PublicationDTO>();
		
		for (PublicationDTO publicationBD : publicationList) {
			if(publicationBD.getFechaPublicacion().equals(fecha)) {
				addPublication.add(publicationBD);		
			}			
		}	
		return addPublication;	
	}

	// BUSCAR POR PROVINCIA
	
	@Override
	public List<PublicationDTO> getFilterForProvincia(HttpSession session, String provincia) throws Exception {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var publicationList = this.getAllBandOrMusicianPublications(session);	
		var addPublication = new ArrayList<PublicationDTO>();
		
		switch(userSession.getRole()) {
		
		case MUSICO:
			for (PublicationDTO publicationBD : publicationList) {
				if(publicationBD.getBandDTO().getProvincia().equalsIgnoreCase(provincia)) {
					addPublication.add(publicationBD);		
				}			
			}
			break;
		case BANDA:
			for (PublicationDTO publicationBD : publicationList) {
				if(publicationBD.getMusicianDTO().getProvincia().equalsIgnoreCase(provincia)) {
					addPublication.add(publicationBD);		
				}			
			}
			break;
			default: 
				throw new Exception("No tiene las credenciales para ver este recurso.");
		}
			
 		return addPublication;	
	} 
	
	// BUSCAR POR INSTRUMENTO
	
	@Override
	public List<PublicationDTO> getFilterForInstrumento(HttpSession session, String instrumento) {
		var publicationList = this.getAllBandOrMusicianPublications(session);		
		var addPublication = new ArrayList<PublicationDTO>();
		
		for (PublicationDTO publicationBD : publicationList) {
			if(publicationBD.getMusicianDTO().getInstrumento().equalsIgnoreCase(instrumento)) {
				addPublication.add(publicationBD);		
			}			
		}		
 		return addPublication;	
	}

}
