package com.encuentro_musical.anuncios.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro_musical.anuncios.dto.PublicationDTO;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.service.IPublicationService;

@RestController
@RequestMapping(path = "/anuncios")
public class PublicationController {

	@Autowired
	private IPublicationService publicationService;

	// ========= ENDPOINTS DE ANUNCIOS QUE PUEDE VER UN MÚSICO LOGUEADO
	// ===================

	// VER TODOS LOS ANUNCIOS DE BANDAS
	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas")
	public ResponseEntity<List<PublicationDTO>> getAllBandPublications(HttpSession session) throws BadRequestException{
		return ResponseEntity.status(HttpStatus.OK).body(publicationService.getAllBandOrMusicianPublications(session));
	}

	
	// FILTROS DE ANUNCIOS

	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/por_genero_musical/{genero_musical}")
	public ResponseEntity<List<PublicationDTO>> getAllGeneroMusical(HttpSession session, @PathVariable String genero_musical) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(publicationService.getFilterForGeneroMusical(session, genero_musical));
	}

	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/por_fecha_publicacion/{fecha_publicacion}")
	public ResponseEntity<List<PublicationDTO>> getAllFecha(HttpSession session,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(publicationService.getFilterForFechaPublicacion(session, fecha_publicacion));
	}

	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/por_provincia/{provincia}")
	public ResponseEntity<List<PublicationDTO>> getAllProvincia(HttpSession session, @PathVariable String provincia) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(publicationService.getFilterForProvincia(session, provincia));
	}

	// ========= ENDPOINTS DE ANUNCIOS QUE PUEDE VER UNA BANDA LOGUEADA
	// ===================

	// VER TODOS LOS ANUNCIOS DE MÚSICOS

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios")
	public ResponseEntity<List<PublicationDTO>> getMusicianPublication(HttpSession session) throws BadRequestException {
		return ResponseEntity.status(HttpStatus.OK).body(publicationService.getAllBandOrMusicianPublications(session));
	}

	// FILTROS DE ANUNCIOS

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_genero_musical/{genero_musical}")
	public ResponseEntity<List<PublicationDTO>> getAllMGeneroMusical(HttpSession session, @PathVariable String genero_musical) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(publicationService.getFilterForGeneroMusical(session, genero_musical));
	}

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_fecha_publicacion/{fecha_publicacion}")
	public ResponseEntity<List<PublicationDTO>> getAllMFecha(HttpSession session,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(publicationService.getFilterForFechaPublicacion(session, fecha_publicacion));
	}

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_provincia/{provincia}")
	public ResponseEntity<List<PublicationDTO>> getAllMProvincia(HttpSession session, @PathVariable String provincia) throws Exception {
		return ResponseEntity.status(HttpStatus.OK)
				.body(publicationService.getFilterForProvincia(session, provincia));
	}

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_instrumento/{instrumento}")
	public ResponseEntity<List<PublicationDTO>> getAllMInstrumento(HttpSession session, @PathVariable String instrumento) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(publicationService.getFilterForInstrumento(session, instrumento));
	} 

}
