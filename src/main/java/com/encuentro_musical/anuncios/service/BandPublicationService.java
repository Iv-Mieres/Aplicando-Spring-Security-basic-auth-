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
import com.encuentro_musical.anuncios.dto.PublicationBDTO;
import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.model.BandPublication;
import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.repository.IBandPublicationRepository;
import com.encuentro_musical.anuncios.repository.IUserBandRepository;

@Service
public class BandPublicationService implements IBandPublicationService {

	@Autowired
	private IBandPublicationRepository bandPublicationRepository;

	@Autowired
	private IUserBandRepository userBandRepository;

	@Autowired
	private ModelMapper modelMapper;

	// GUARDAR ANUNCIO DE UNA BANDA

	@Override
	public void savePublication(HttpSession session, BandPublication bandPublication) {
		var bandSession = (UserBand) session.getAttribute("usersession");
		var bandPublicationSave = bandPublication;
		bandPublicationSave.setUserBand(bandSession);
		bandPublicationRepository.save(bandPublicationSave);
	}

	// MOSTRAR TODOS LOS ANUNCIOS PAGINADOS DE TODAS LAS BANDAS

	@Override
	public List<PublicationBDTO> getAllPublications() {
		int pageNumber = 0;
		int pageSize = 5;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<BandPublication> listPublicationsBD = bandPublicationRepository.findAll(pageable);
		var listBand = new ArrayList<PublicationBDTO>();

		for (BandPublication bandPublication : listPublicationsBD) {
			PublicationBDTO publicationDTO = modelMapper.map(bandPublication, PublicationBDTO.class);
			BandDTO bandDTO = modelMapper.map(bandPublication.getUserBand(), BandDTO.class);
			publicationDTO.setBandDTO(bandDTO);
			listBand.add(publicationDTO);
		}

		return listBand;
	}

	// ELIMINADO LÓGICO DE UN ANUNCIO

	@Override
	public void deleteBandPublication(HttpSession session, Long idPublication) throws Exception {
		UserBand bandSession = (UserBand) session.getAttribute("usersession");
		BandPublication bandPublicationBD = bandPublicationRepository.findById(idPublication)
				.orElseThrow(() -> new Exception("El id " + idPublication + " no existe. Ingrese un id válido"));

		if (bandPublicationBD.getUserBand().getIdBand().equals(bandSession.getIdBand())) {
			bandPublicationBD.setEliminado(true);
		}
		bandPublicationRepository.save(bandPublicationBD);
	}

	// ============ FILTRAR ANUNCIOS POR ... =================

	// FILTRAR POR GENERO MÚSICAL

	@Override
	public List<PublicationBDTO> getPublicationByGeneroMusical(String generoMusical) {

		var listBandPublication = new ArrayList<PublicationBDTO>();

		for (BandPublication bandPublication : this.getAllBandPublications()) {
			if (bandPublication.getGeneroMusical().equals(generoMusical)) {
				PublicationBDTO publicationDTO = modelMapper.map(bandPublication, PublicationBDTO.class);
				BandDTO userBand = modelMapper.map(bandPublication.getUserBand(), BandDTO.class);
				publicationDTO.setBandDTO(userBand);
				listBandPublication.add(publicationDTO);
			}
		}
		return listBandPublication;
	}

	// FILTRAR POR FECHA DE PUBLICACIÓN
	
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
	
	// FILTRAR POR PROVINCIA

	@Override
	public List<PublicationBDTO> getPublicationByProvincia(String provincia) {
		var listBandPublication = new ArrayList<PublicationBDTO>();

		for (UserBand userBand : this.getAllBands()) {
			if (userBand.getProvincia().equals(provincia)) {
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
