package com.encuentro_musical.anuncios.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro_musical.anuncios.dto.PaginadoDTO;
import com.encuentro_musical.anuncios.dto.PublicationBDTO;
import com.encuentro_musical.anuncios.dto.PublicationMDTO;
import com.encuentro_musical.anuncios.service.IPublicationBandService;
import com.encuentro_musical.anuncios.service.IPublicationMusicianService;

@RestController
@RequestMapping(path = "/anuncios")
public class PublicationController {

	@Autowired
	private IPublicationBandService bandPublicationService;

	@Autowired
	private IPublicationMusicianService musicianPublicationService;

	// ========= ENDPOINTS DE ANUNCIOS QUE PUEDE VER UN MÚSICO LOGUEADO
	// ===================

	// VER TODOS LOS ANUNCIOS DE BANDAS
	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas")
	public ResponseEntity<PaginadoDTO> getBandPublication(
			@RequestParam(required = false, name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(required = false, name = "pageSize", defaultValue = "5") int pageSize) {
		return ResponseEntity.status(HttpStatus.OK).body(bandPublicationService.getAllPublications(pageNumber, pageSize));
	}

	// FILTROS DE ANUNCIOS

	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/por_genero_musical/{genero_musical}")
	public ResponseEntity<List<PublicationBDTO>> getAllGeneroMusical(@PathVariable String genero_musical) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(bandPublicationService.getPublicationByGeneroMusical(genero_musical));
	}

	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/por_fecha_publicacion/{fecha_publicacion}")
	public ResponseEntity<List<PublicationBDTO>> getAllFecha(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(bandPublicationService.getPublicationByFechaPublicacion(fecha_publicacion));
	}

	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/por_provincia/{provincia}")
	public ResponseEntity<List<PublicationBDTO>> getAllProvincia(@PathVariable String provincia) {
		return ResponseEntity.status(HttpStatus.OK).body(bandPublicationService.getPublicationByProvincia(provincia));
	}

	// ========= ENDPOINTS DE ANUNCIOS QUE PUEDE VER UNA BANDA LOGUEADA
	// ===================

	// VER TODOS LOS ANUNCIOS DE MÚSICOS

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios")
	public ResponseEntity<List<PublicationMDTO>> getMusicianPublication() {
		return ResponseEntity.status(HttpStatus.OK).body(musicianPublicationService.getAllPublications());
	}

	// FILTROS DE ANUNCIOS

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_genero_musical/{genero_musical}")
	public ResponseEntity<List<PublicationMDTO>> getAllMGeneroMusical(@PathVariable String genero_musical) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(musicianPublicationService.getPublicationByGeneroMusical(genero_musical));
	}

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_fecha_publicacion/{fecha_publicacion}")
	public ResponseEntity<List<PublicationMDTO>> getAllMFecha(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(musicianPublicationService.getPublicationByFechaPublicacion(fecha_publicacion));
	}

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_provincia/{provincia}")
	public ResponseEntity<List<PublicationMDTO>> getAllMProvincia(@PathVariable String provincia) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(musicianPublicationService.getPublicationByProvincia(provincia));
	}

	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/musicos/ver_anuncios/por_instrumento/{instrumento}")
	public ResponseEntity<List<PublicationMDTO>> getAllMInstrumento(@PathVariable String instrumento) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(musicianPublicationService.getPublicationByInstrumento(instrumento));
	}

}
