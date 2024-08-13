package com.movie.cartelera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movie.cartelera.models.MovieModel;

public interface MovieRepository extends JpaRepository<MovieModel, Long>{

	
}
