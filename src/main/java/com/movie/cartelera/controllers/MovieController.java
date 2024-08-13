package com.movie.cartelera.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.cartelera.models.MovieModel;
import com.movie.cartelera.repositories.MovieRepository;

@RestController
@RequestMapping("api/movies")
public class MovieController {

	@Autowired
	private MovieRepository movieRepository;
	
	//Metodo para obtener todos los registros de peliculas.
	@CrossOrigin
	@GetMapping("/all")
	public List<MovieModel> getAllMovies(){
		return movieRepository.findAll();
	}
		
	@CrossOrigin
	@GetMapping("/detail/{id}")
	public ResponseEntity<MovieModel> getMovieById(@PathVariable Long id){
		Optional<MovieModel> movie = movieRepository.findById(id);
		
		return movie.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build() );
	}
		
	@CrossOrigin
	@PostMapping("/create")
	public ResponseEntity<MovieModel> createMovie(@RequestBody MovieModel movie) {
		
		MovieModel saveMovie= movieRepository.save(movie);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveMovie);
	}
	
	@CrossOrigin
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
		
		if(!movieRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		movieRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
		
	@CrossOrigin
	@PutMapping("/updated/{id}")
	public ResponseEntity<MovieModel> updateMovie(@PathVariable Long id,@RequestBody MovieModel updatedMovie){
		
		if(!movieRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		updatedMovie.setId(id);
		MovieModel savedMovie = movieRepository.save(updatedMovie);
		
		return ResponseEntity.ok(savedMovie);
		
	}
		
	@CrossOrigin
	@GetMapping("/vote/{id}/{rating}")
	public ResponseEntity<MovieModel> voteMovie(@PathVariable Long id, @PathVariable double rating ){
		if(!movieRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		Optional<MovieModel> optional = movieRepository.findById(id);
		MovieModel movie = optional.get();
		
		double newRating = ((movie.getVotes() * movie.getRating()) + rating ) / (movie.getVotes() + 1) ;
		
		movie.setVotes(movie.getVotes() + 1);
		movie.setRating(newRating);
		
		MovieModel savedMovie = movieRepository.save(movie);
		return ResponseEntity.ok(savedMovie);

		
	}
	                         
}
