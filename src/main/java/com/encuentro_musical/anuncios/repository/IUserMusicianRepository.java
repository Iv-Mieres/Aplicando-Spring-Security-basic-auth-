package com.encuentro_musical.anuncios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.encuentro_musical.anuncios.model.UserMusician;

@Repository
public interface IUserMusicianRepository extends JpaRepository<UserMusician, Long> {
	public UserMusician findByuserName(String userName);
	public UserMusician findByEmail(String email);
	public List<UserMusician> findByInstrumento(String instrumento);
	public boolean existsByEmail(String email);
}
