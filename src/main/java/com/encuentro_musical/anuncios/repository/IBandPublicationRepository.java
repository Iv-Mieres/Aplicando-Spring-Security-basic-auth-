package com.encuentro_musical.anuncios.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encuentro_musical.anuncios.model.BandPublication;

@Repository
public interface IBandPublicationRepository extends JpaRepository<BandPublication, Long> {

	public BandPublication findAllByFechaPublicacion(LocalDate fechaPublicacion);
	public BandPublication findAllByGeneroMusical(String generoMusical);

}
