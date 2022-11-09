package com.encuentro_musical.anuncios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.encuentro_musical.anuncios.model.Publication;

@Repository
public interface IPublicationRepository extends JpaRepository<Publication, Long> {

}
