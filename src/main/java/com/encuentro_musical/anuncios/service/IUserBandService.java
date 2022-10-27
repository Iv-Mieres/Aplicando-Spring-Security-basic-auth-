package com.encuentro_musical.anuncios.service;

import javax.servlet.http.HttpSession;

import com.encuentro_musical.anuncios.dto.MyBandProfileDTO;
import com.encuentro_musical.anuncios.dto.RegisterBDTO;
import com.encuentro_musical.anuncios.model.BandPublication;
import com.encuentro_musical.anuncios.model.UserBand;

public interface IUserBandService {

	public UserBand saveUserBand(RegisterBDTO registerBand) throws Exception;
	
	public MyBandProfileDTO myProfile(HttpSession session);

	public void updateBand(HttpSession session, UserBand userBand);

	public void updateBandPublication(HttpSession session, Long idBandPublication, BandPublication bandPublication) throws Exception;
	
	public void deleteBand(HttpSession session);
}
