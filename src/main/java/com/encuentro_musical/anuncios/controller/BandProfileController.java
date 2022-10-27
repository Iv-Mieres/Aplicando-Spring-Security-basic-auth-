package com.encuentro_musical.anuncios.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro_musical.anuncios.dto.MyBandProfileDTO;
import com.encuentro_musical.anuncios.dto.RegisterBDTO;
import com.encuentro_musical.anuncios.model.BandPublication;
import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.service.IBandPublicationService;
import com.encuentro_musical.anuncios.service.IUserBandService;


@RestController
@RequestMapping(path = "bandas")
public class BandProfileController {
	
	@Autowired
	private IBandPublicationService bandPublicationService;

	@Autowired
	private IUserBandService userBandService;
	
	//================= EDNPOINTS PARA BANDA ==========================
	
	// CREAR USUARIO BANDA
	
	@PostMapping("/crear_usuario")
	public ResponseEntity<String> saveBand(@RequestBody @Valid RegisterBDTO registerBand){
		try {
			userBandService.saveUserBand(registerBand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Su usuario fue creado correctamente!");
	}
	
	// CREAR ANUNCIO
	
	@PreAuthorize("hasRole('BANDA')")
	@PostMapping("/crear_anuncio")
	public ResponseEntity<String> saveBandPublication(HttpSession session,  @RequestBody @Valid BandPublication bandPublication){
		bandPublicationService.savePublication(session, bandPublication);
		return ResponseEntity.status(HttpStatus.CREATED).body("Su anuncio fue creado correctamente!");
	}
	
	// VER PERFIL DE BANDA LOGUEADA
	
	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/mi_perfil")
	public ResponseEntity<MyBandProfileDTO> getUserLogin(HttpSession session){
		return ResponseEntity.status(HttpStatus.OK).body(userBandService.myProfile(session));
	}
	
	// EDITAR PERFIL DE BANDA LOGUEADA
	
	@PreAuthorize("hasRole('BANDA')")
	@GetMapping("/mi_perfil/editar")
	public ResponseEntity<String> updateUserLogin(HttpSession session, @RequestBody UserBand userBand){
		userBandService.updateBand(session, userBand);
		return ResponseEntity.status(HttpStatus.OK).body("Los datos se actualizaron correctamente!");
	}
	
	// EDITAR ANUNCIO DE BANDA LOGUEADA
	
	@PreAuthorize("hasRole('BANDA')")
	@PostMapping("/mi_perfil/editar_anuncio/{id_anuncio}")
	public ResponseEntity<String> updatePublication(HttpSession session, @PathVariable Long id_anuncio,
													@RequestBody @Valid BandPublication bandPublication) throws Exception{
		userBandService.updateBandPublication(session, id_anuncio, bandPublication);
		return ResponseEntity.status(HttpStatus.OK).body("Su anuncio de editó correctamente!");
	}
	
	// ELIMINAR USUARIO BANDA LOGUEADO
	
	@PreAuthorize("hasRole('BANDA')")
	@PostMapping("/mi_perfil/eliminar")
	public ResponseEntity<String> deleteBand(HttpSession session){
			userBandService.deleteBand(session);
		return ResponseEntity.status(HttpStatus.OK).body("Su cuenta usuario ha sido eliminada");
	}
	
	
}
