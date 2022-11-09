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
import com.encuentro_musical.anuncios.dto.PaginadoDTO;
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

	// MUESTRA LAS PUBLICACIONES PEGINADAS DE BANDAS O MUSICOS

	@Override
	public PaginadoDTO getAllBandOrMusicianPublications(HttpSession session, int pageNumber, int pageSize) {
		UserModel userSession = (UserModel) session.getAttribute("usersession");

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Publication> listPublicationsBD = publicationRepository.findAll(pageable);
		var paginadoDTO = modelMapper.map(listPublicationsBD, PaginadoDTO.class);

		switch (userSession.getRole()) {
		case MUSICO://Muestra las publicaciones de músicos si el role de userSession es MUSICO
			var listPublicationDTO = new ArrayList<PublicationDTO>();
			paginadoDTO.setPublicationDTO(listPublicationDTO);

			for (Publication publicationBD : listPublicationsBD) {
				if (Objects.isNull(publicationBD.getUserMusician()) && !Objects.isNull(publicationBD.getUserBand())) {
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					BandDTO bandDTO = modelMapper.map(publicationBD.getUserBand(), BandDTO.class);
					publicationDTO.setBandDTO(bandDTO);
					listPublicationDTO.add(publicationDTO);
				}
			}
			return paginadoDTO;

		case BANDA://Muestra las publicaciones de bandas si el role de userSession es BANDA
			var listPublicationMDTO = new ArrayList<PublicationDTO>();
			paginadoDTO.setPublicationDTO(listPublicationMDTO);

			for (Publication publicationBD : listPublicationsBD) {
				if (Objects.isNull(publicationBD.getUserBand()) && !Objects.isNull(publicationBD.getUserMusician())) {
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					MusicianDTO musicianDTO = modelMapper.map(publicationBD.getUserMusician(), MusicianDTO.class);
					publicationDTO.setMusicianDTO(musicianDTO);
					listPublicationMDTO.add(publicationDTO);
				}
			}
			return paginadoDTO;
		default:
			return null;
		}
	}

	// ELIMINAR ANUNCIO COMO USUARIO MUSICO, BANDA O ADMIN

	@Override
	public void deletePublication(HttpSession session, Long idPublication) throws BadRequestException {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var publicationBD = publicationRepository.findById(idPublication)
				.orElseThrow(() -> new BadRequestException("El id " + idPublication + " no es correcto. Ingrese un id válido"));
		
		switch (userSession.getRole()) {

		case MUSICO://Elimina la publicación si el role de userSession es MUSICO y el email de la publicación
					// de base de datos coincide con el email del userSession
			if (Objects.isNull(publicationBD.getUserMusician()) || 
					!publicationBD.getUserMusician().getEmail().equals(userSession.getEmail())) {
				throw new BadRequestException("El id " + idPublication + " no es correcto. Ingrese un id válido");
			}
			publicationRepository.delete(publicationBD);
			break;
		case BANDA://Misma función que el caso de MUSICO, pero para BANDA 
			if (Objects.isNull(publicationBD.getUserBand()) ||
					!publicationBD.getUserBand().getEmail().equals(userSession.getEmail())) {
				throw new BadRequestException("El id " + idPublication + " no es correcto. Ingrese un id válido");
			}
			publicationRepository.delete(publicationBD);
			break;
		case ADMIN://Si el role del userSession es ADMIN puede eliminar cualquier anuncio
			publicationRepository.delete(publicationBD);
		default:
			throw new BadRequestException("Role no asignado");
		}

	}
	
	//============== FILTROS DE BUSQUEDA ==============
	
	// BUSCAR POR GENERO MUSICAL

	@Override
	public List<PublicationDTO> getPublicationByGeneroMusical(HttpSession session, String generoMusical) {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var listPublication = new ArrayList<PublicationDTO>();
		
		switch(userSession.getRole()) {
		
		case MUSICO:
			for (Publication publicationBD : publicationRepository.findAll()) {
				if (publicationBD.getGeneroMusical().equalsIgnoreCase(generoMusical) 
						&& !Objects.isNull(publicationBD.getUserBand())) {
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					BandDTO userBand = modelMapper.map(publicationBD.getUserBand(), BandDTO.class);
					publicationDTO.setBandDTO(userBand);
					listPublication.add(publicationDTO);
				}
			}
			return listPublication;
		case BANDA:
			for (Publication publicationBD : publicationRepository.findAll()) {
				if (publicationBD.getGeneroMusical().equalsIgnoreCase(generoMusical) 
						&& !Objects.isNull(publicationBD.getUserMusician())) {
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					MusicianDTO userMusician = modelMapper.map(publicationBD.getUserMusician(), MusicianDTO.class);
					publicationDTO.setMusicianDTO(userMusician);
					listPublication.add(publicationDTO);
				}
			}
			return listPublication;	
			default: 
				return null;
		}

		
	}

	// BUSCAR POR FECHA DE PUBLICACIÓN
	
	@Override
	public List<PublicationDTO> getPublicationByFechaPublicacion(HttpSession session, LocalDate fechaPublicacion) {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var listPublication = new ArrayList<PublicationDTO>();
		
		
		switch(userSession.getRole()) {
		
		case MUSICO:
			for (Publication publicationBD : publicationRepository.findAll()) {
				if (publicationBD.getFechaPublicacion().equals(fechaPublicacion) 
						&& !Objects.isNull(publicationBD.getUserBand())) {
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					BandDTO userBand = modelMapper.map(publicationBD.getUserBand(), BandDTO.class);
					publicationDTO.setBandDTO(userBand);
					listPublication.add(publicationDTO);
				}
			}
			return listPublication;
		case BANDA:
			for (Publication publicationBD : publicationRepository.findAll()) {
				if (publicationBD.getFechaPublicacion().equals(fechaPublicacion) 
						&& !Objects.isNull(publicationBD.getUserMusician())) {
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					MusicianDTO userMusician = modelMapper.map(publicationBD.getUserMusician(), MusicianDTO.class);
					publicationDTO.setMusicianDTO(userMusician);
					listPublication.add(publicationDTO);
				}
			}
			return listPublication;	
			default: 
				return null;
		}
	}

	// BUSCAR POR PROVINCIA
	
	@Override
	public List<PublicationDTO> getPublicationByProvincia(HttpSession session, String provincia) {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var listPublication = new ArrayList<PublicationDTO>();
	
		switch(userSession.getRole()) {
		
		case MUSICO:
			for (Publication publicationBD : publicationRepository.findAll()) {
				 if(!Objects.isNull(publicationBD.getUserBand())
						 && publicationBD.getUserBand().getProvincia().equalsIgnoreCase(provincia)){
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					BandDTO userBand = modelMapper.map(publicationBD.getUserBand(), BandDTO.class);
					publicationDTO.setBandDTO(userBand);
					listPublication.add(publicationDTO);				
				}			
			}
			return listPublication;
			
			
		case BANDA:
			for (Publication publicationBD : publicationRepository.findAll()) {
				if (!Objects.isNull(publicationBD.getUserMusician()) 
						&& publicationBD.getUserMusician().getProvincia().equalsIgnoreCase(provincia)){
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					MusicianDTO userMusician = modelMapper.map(publicationBD.getUserMusician(), MusicianDTO.class);
					publicationDTO.setMusicianDTO(userMusician);
					listPublication.add(publicationDTO);
				}
			}
			return listPublication;	
			default: 
				return null;
		}
	} 
	
	// BUSCAR POR INSTRUMENTO
	
	@Override
	public List<PublicationDTO> getPublicationByInstrumento(HttpSession session, String instrumento) {
		UserModel userSession = (UserModel) session.getAttribute("usersession");
		var listMusicianPublication = new ArrayList<PublicationDTO>();
		
		switch(userSession.getRole()) {
			
		case BANDA:
			for (Publication publicationBD : publicationRepository.findAll()) {
				if(!Objects.isNull(publicationBD.getUserMusician()) 
						&& publicationBD.getUserMusician().getInstrumento().equalsIgnoreCase(instrumento)){
					PublicationDTO publicationDTO = modelMapper.map(publicationBD, PublicationDTO.class);
					MusicianDTO userMusician = modelMapper.map(publicationBD.getUserMusician(), MusicianDTO.class);
					publicationDTO.setMusicianDTO(userMusician);
					listMusicianPublication.add(publicationDTO);
				}
			}
			return listMusicianPublication;	
			default: 
				return null;
		}
	}

}
