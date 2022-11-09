package com.encuentro_musical.anuncios.service;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.MyMusicianProfileDTO;
import com.encuentro_musical.anuncios.model.Publication;
import com.encuentro_musical.anuncios.model.UserMusician;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;

public interface IUserMusicianService {

	public UserMusician saveUserMusician(UserMusician registerMusicianDTO) throws BadRequestException;

	public MyMusicianProfileDTO myMusicianProfile(HttpSession session);

	public void updateMusician(HttpSession session, UserMusician userMusician) throws BadRequestException;

	public void updateMusicianPublication(HttpSession session, Long idMusicianPublication,
											Publication musicianPublication) throws BadRequestException;

	public void deleteMusician(HttpSession session);

}
