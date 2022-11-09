package com.encuentro_musical.anuncios.service;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.MyBandProfileDTO;
import com.encuentro_musical.anuncios.model.Publication;
import com.encuentro_musical.anuncios.model.UserBand;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;

public interface IUserBandService {

	public UserBand saveUserBand(UserBand registerBand) throws BadRequestException;
	
	public MyBandProfileDTO myProfile(HttpSession session);

	public void updateBand(HttpSession session, UserBand userBand)  throws BadRequestException;

	public void updateBandPublication(HttpSession session, Long idBandPublication, Publication bandPublication) throws Exception;
	
	public void deleteBand(HttpSession session);
}
