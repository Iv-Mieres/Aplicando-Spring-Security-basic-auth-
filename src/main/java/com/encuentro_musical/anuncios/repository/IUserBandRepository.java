package com.encuentro_musical.anuncios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encuentro_musical.anuncios.model.UserBand;
;

@Repository
public interface IUserBandRepository extends JpaRepository<UserBand, Long>{
	
	public UserBand findByuserName(String userName);
	public UserBand findByEmail(String email);
	public List<UserBand> findByProvincia(String provincia);
	public List<UserBand> findByLocalidad(String localidad);
	public boolean existsByUserName(String userName);
	public boolean existsByEmail(String email);
}
