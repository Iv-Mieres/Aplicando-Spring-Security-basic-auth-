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
import org.springframework.web.bind.annotation.RestController;

import com.encuentro_musical.anuncios.dto.PublicationBDTO;
import com.encuentro_musical.anuncios.service.IBandPublicationService;

@RestController
public class PublicationController {
	

	@Autowired
	private IBandPublicationService bandPublicationService;
	
	
	//========= ENDPOINTS DE ANUNCIOS QUE PUEDE VER UN MÚSICO LOGUEADO ===================
	
	
	// VER TODOS LOS ANUNCIOS DE BANDAS
	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/ver_anuncios")
	public ResponseEntity<List<PublicationBDTO>> getBandPublication(){
		return ResponseEntity.status(HttpStatus.OK).body(bandPublicationService.getAllPublications());
	}
	
    // FILTROS DE ANUNCIOS
	
	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/ver_anuncios/por_genero_musical/{genero_musical}")
	public ResponseEntity<List<PublicationBDTO>> getAllGeneroMusical(@PathVariable String genero_musical){
		return ResponseEntity.status(HttpStatus.OK).body(bandPublicationService.getPublicationByGeneroMusical(genero_musical));
	}
	
	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/ver_anuncios/por_fecha_publicacion/{fecha_publicacion}")
	public ResponseEntity<List<PublicationBDTO>> getAllFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion){
		return ResponseEntity.status(HttpStatus.OK).body(bandPublicationService.getPublicationByFechaPublicacion(fecha_publicacion));
	}
	
	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/bandas/ver_anuncios/por_provincia/{provincia}")
	public ResponseEntity<List<PublicationBDTO>> getAllFecha(@PathVariable String provincia){
		return ResponseEntity.status(HttpStatus.OK).body(bandPublicationService.getPublicationByProvincia(provincia));
	}
	
}