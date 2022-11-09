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

import com.encuentro_musical.anuncios.dto.MyMusicianProfileDTO;
import com.encuentro_musical.anuncios.model.Publication;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.service.IPublicationService;
import com.encuentro_musical.anuncios.service.IUserMusicianService;

@RestController
@RequestMapping(path = "musicos")
public class MusicianProfileController {

	@Autowired
	private IUserMusicianService userMusicianService;

	@Autowired
	private IPublicationService publicationService;

	// ================= EDNPOINTS PARA MÚSICO ==========================

	// CREAR MÚSICO

	@PostMapping("/crear_usuario")
	public ResponseEntity<String> saveMusician(@RequestBody @Valid UserMusician registerMusician)
			throws BadRequestException {
		userMusicianService.saveUserMusician(registerMusician);
		return ResponseEntity.status(HttpStatus.CREATED).body("Su usuario fue creado correctamente");
	}

	// CREAR ANUNCIO

	@PreAuthorize("hasRole('MUSICO')")
	@PostMapping("/crear_anuncio")
	public ResponseEntity<String> saveMusicianPublication(HttpSession session,
			@RequestBody @Valid Publication musicianPublication) {
		publicationService.savePublication(session, musicianPublication);
		return ResponseEntity.status(HttpStatus.CREATED).body("Su anuncio se publicó correctamente!");
	}

	// VER PERFIL DE MÚSICO LOGUEADO

	@PreAuthorize("hasRole('MUSICO')")
	@GetMapping("/mi_perfil")
	public ResponseEntity<MyMusicianProfileDTO> myProfile(HttpSession session) {
		return ResponseEntity.status(HttpStatus.OK).body(userMusicianService.myMusicianProfile(session));

	}

	// EDITAR PERFIL DE MÚSICO LOGUEADO

	@PreAuthorize("hasRole('MUSICO')")
	@PostMapping("/mi_perfil/editar")
	public ResponseEntity<String> updateMusician(HttpSession session, @RequestBody @Valid UserMusician userMusician)
			throws BadRequestException {
		userMusicianService.updateMusician(session, userMusician);
		return ResponseEntity.status(HttpStatus.OK).body("Los datos se actualizaron correctamente!");
	}

	// EDITAR ANUNCIO DE MÚSICO

	@PreAuthorize("hasRole('MUSICO')")
	@PostMapping("/mi_perfil/editar_anuncio/{idMusicianPublication}")
	public ResponseEntity<String> editPublication(HttpSession session, @PathVariable Long idMusicianPublication,
			@RequestBody @Valid Publication musicianPublication) throws BadRequestException {
		userMusicianService.updateMusicianPublication(session, idMusicianPublication, musicianPublication);
		return ResponseEntity.status(HttpStatus.OK).body("El anuncio se actualizó correctamente!");
	}

	// ELIMINAR PERFIL DE MÚSICO LOGUEADO

	@PreAuthorize("hasRole('MUSICO')")
	@PostMapping("/mi_perfil/eliminar")
	public ResponseEntity<String> deleteUser(HttpSession session) {
		userMusicianService.deleteMusician(session);
		return ResponseEntity.status(HttpStatus.OK).body("Su cuenta de usuario ha sido eliminada");
	}

}
