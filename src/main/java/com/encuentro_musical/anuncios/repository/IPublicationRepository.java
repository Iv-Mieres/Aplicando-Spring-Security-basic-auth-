package com.encuentro_musical.anuncios.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.encuentro_musical.anuncios.model.Publication;

@Repository
public interface IPublicationRepository extends JpaRepository<Publication, Long> {
	List<Publication> findByFechaPublicacion(LocalDate fechaPublicacion);
	List<Publication> findByGeneroMusical(String generoMusical);
	
}
