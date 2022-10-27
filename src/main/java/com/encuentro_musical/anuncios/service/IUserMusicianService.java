package com.encuentro_musical.anuncios.service;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.MyMusicianProfileDTO;
import com.encuentro_musical.anuncios.dto.RegisterMDTO;
import com.encuentro_musical.anuncios.model.MusicianPublication;
import com.encuentro_musical.anuncios.model.UserMusician;

public interface IUserMusicianService {

	public UserMusician saveUserMusician(RegisterMDTO registerMusicianDTO) throws Exception;

	public MyMusicianProfileDTO myProfile(HttpSession session);

	public void updateMusician(HttpSession session, UserMusician userMusician);

	public void updateMusicianPublication(HttpSession session, Long idMusicianPublication,
											MusicianPublication musicianPublication) throws Exception;

	public void deleteMusician(HttpSession session);

}
