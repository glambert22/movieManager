package com.secureally.demo.rest;

import com.secureally.demo.dao.entities.Movies;
import com.secureally.demo.service.MovieService;
import com.secureally.demo.utils.ResponsePage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieResource {

    private final MovieService service;

    @PostMapping
    public ResponseEntity<ResponsePage<Movies>> createMovie(@RequestBody Movies movie, HttpServletRequest request) throws URISyntaxException {
        Movies persisted = service.createMoviesEntry(movie);
        URI location = new URI(request.getRequestURL().append("/").append(persisted.getId()).toString());
        return ResponseEntity.created(location).body(new ResponsePage<>(persisted));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponsePage<Movies>> getMovies(@Min(0) @RequestParam(defaultValue = "0") Integer page, @Min(0) @Max(10) @RequestParam(defaultValue = "5") Integer size) {
        ResponsePage response = service.getAllMovies(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePage<Movies>> getMovieById(@PathVariable Integer id) {
        ResponsePage response = new ResponsePage(service.getMovieById(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMovie(@PathVariable Integer id, @RequestBody Movies movie) {
        movie.setId(id);
        service.updateMovie(movie);
        return ResponseEntity.accepted().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> purgeMovie(@PathVariable Integer id) {
        service.deleteMovie(id);
        return ResponseEntity.accepted().build();
    }

}
