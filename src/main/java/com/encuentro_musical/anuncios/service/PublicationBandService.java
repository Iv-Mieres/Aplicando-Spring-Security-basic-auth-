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
import com.encuentro_musical.anuncios.dto.PaginadoDTO;
import com.encuentro_musical.anuncios.dto.PublicationBDTO;
import com.encuentro_musical.anuncios.model.BandPublication;
import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.repository.IBandPublicationRepository;
import com.encuentro_musical.anuncios.repository.IUserBandRepository;

@Service
public class PublicationBandService implements IPublicationBandService {

	@Autowired
	private IBandPublicationRepository bandPublicationRepository;

	@Autowired
	private IUserBandRepository userBandRepository;

	@Autowired
	private ModelMapper modelMapper;
	

	// GUARDAR ANUNCIO DE UNA BANDA

	@Override
	public void savePublication(HttpSession session, BandPublication bandPublication) throws BadRequestException {
			
		if(!bandPublication.getFechaPublicacion().equals(LocalDate.now())) {
			throw new BadRequestException("La fecha ingresada debe ser igual a la fecha de hoy: " + LocalDate.now());
		}
		var bandSession = (UserBand) session.getAttribute("usersession");
		var bandPublicationSave = bandPublication;
		bandPublicationSave.setUserBand(bandSession);
		
		bandPublicationRepository.save(bandPublicationSave);
	}

	// MOSTRAR TODOS LOS ANUNCIOS PAGINADOS DE TODAS LAS BANDAS

	
	@Override
	public PaginadoDTO getAllPublications(int pageNumber, int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<BandPublication> listPublicationsBD = bandPublicationRepository.findAll(pageable);
		
		var listPublicationBDTO = new ArrayList<PublicationBDTO>();
		var paginadoDTO = modelMapper.map(listPublicationsBD, PaginadoDTO.class);
		paginadoDTO.setPublicationBDTO(listPublicationBDTO);

		for (BandPublication bandPublication : listPublicationsBD) {		
			if(bandPublication.getUserBand().isEnabled() == true) {	
				
			PublicationBDTO publicationDTO = modelMapper.map(bandPublication, PublicationBDTO.class);	
			BandDTO bandDTO = modelMapper.map(bandPublication.getUserBand(), BandDTO.class);	
			publicationDTO.setBandDTO(bandDTO);
			
			listPublicationBDTO.add(publicationDTO);
			}
		}			
		return paginadoDTO;
	}

	// ELIMINAR ANUNCIO

	@Override
	public void deleteBandPublication(HttpSession session, Long idPublication) throws BadRequestException {
		var bandSession = (UserBand) session.getAttribute("usersession");
		var bandPublicationBD = bandPublicationRepository.findById(idPublication).orElse(null);

		if (!bandPublicationBD.getUserBand().getIdBand().equals(bandSession.getIdBand())) {
			throw new BadRequestException("El id " + idPublication + " no es correcto. Ingrese un id válido");
		}
		bandPublicationRepository.delete(bandPublicationBD);
	}

	// ============ FILTRAR ANUNCIOS POR ... =================

	@Override
	public List<PublicationBDTO> getPublicationByGeneroMusical(String generoMusical) {

		var listBandPublication = new ArrayList<PublicationBDTO>();

		for (BandPublication bandPublication : this.getAllBandPublications()) {
			if (bandPublication.getGeneroMusical().equalsIgnoreCase(generoMusical)) {
				PublicationBDTO publicationDTO = modelMapper.map(bandPublication, PublicationBDTO.class);
				BandDTO userBand = modelMapper.map(bandPublication.getUserBand(), BandDTO.class);
				publicationDTO.setBandDTO(userBand);
				listBandPublication.add(publicationDTO);
			}
		}
		return listBandPublication;
	}

	@Override
	public List<PublicationBDTO> getPublicationByFechaPublicacion(LocalDate fechaPublicacion) {
		var listBandPublication = new ArrayList<PublicationBDTO>();

		for (BandPublication bandPublication : this.getAllBandPublications()) {
			if (bandPublication.getFechaPublicacion().equals(fechaPublicacion)) {
				PublicationBDTO publicationDTO = modelMapper.map(bandPublication, PublicationBDTO.class);
				BandDTO userBand = modelMapper.map(bandPublication.getUserBand(), BandDTO.class);
				publicationDTO.setBandDTO(userBand);
				listBandPublication.add(publicationDTO);
			}
		}
		return listBandPublication;
	}

	@Override
	public List<PublicationBDTO> getPublicationByProvincia(String provincia) {
		var listBandPublication = new ArrayList<PublicationBDTO>();

		for (UserBand userBand : this.getAllBands()) {
			if (userBand.getProvincia().equalsIgnoreCase(provincia)
					&& !userBand.isEnabled()) {
				for (BandPublication bandPublication : userBand.getListPublicationsBand()) {
					if (!Objects.isNull(bandPublication)) {
						PublicationBDTO publicationDTO = modelMapper.map(bandPublication, PublicationBDTO.class);
						BandDTO bandDTO = modelMapper.map(userBand, BandDTO.class);
						publicationDTO.setBandDTO(bandDTO);
						listBandPublication.add(publicationDTO);
					}

				}

			}
		}
		return listBandPublication;
	}
	
	public List<BandPublication> getAllBandPublications() {
		return bandPublicationRepository.findAll();
	}

	public List<UserBand> getAllBands() {
		return userBandRepository.findAll();
	}

}
