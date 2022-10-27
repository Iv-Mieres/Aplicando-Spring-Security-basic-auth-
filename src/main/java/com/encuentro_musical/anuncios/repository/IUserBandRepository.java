package com.encuentro_musical.anuncios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encuentro_musical.anuncios.model.UserBand;
;

@Repository
public interface IUserBandRepository extends JpaRepository<UserBand, Long>{
	
	public UserBand findByuserName(String userName);
	public UserBand findByEmail(String email);
	public UserBand findByProvincia(String provincia);
	public UserBand findByLocalidad(String localidad);
	public boolean existsByEmail(String email);
}
