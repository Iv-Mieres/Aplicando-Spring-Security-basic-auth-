package com.encuentro_musical.anuncios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encuentro_musical.anuncios.model.MusicianPublication;

@Repository
public interface IMusicianPublicationRepository extends JpaRepository<MusicianPublication, Long> {

}
