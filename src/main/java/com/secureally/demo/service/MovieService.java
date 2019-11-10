package com.secureally.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.secureally.demo.dao.entities.Movies;
import com.secureally.demo.dao.repository.MovieRepository;
import com.secureally.demo.exceptions.MovieManagerException;
import com.secureally.demo.rest.MovieResource;
import com.secureally.demo.utils.JsonUtils;
import com.secureally.demo.utils.ResponsePage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import static com.secureally.demo.constants.ApplicationErrorCodes.INVALID_MOVIE_ATTRIBUTE;
import static com.secureally.demo.constants.ApplicationErrorCodes.INVALID_MOVIE_ERROR;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service

@Validated
public class MovieService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository movieRepository;

    public Movies createMoviesEntry(Movies detachedMovie) {
        return movieRepository.saveAndFlush(detachedMovie);
    }

    public Movies getMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() ->
                        new MovieManagerException(String.format("Movie with %d not found", id), BAD_REQUEST,
                                INVALID_MOVIE_ERROR));
    }

    public ResponsePage getAllMovies(int page, int size) {
        String uri = buildQueryUri(page, size);
        Page response = movieRepository.findAll(PageRequest.of(page, size, Sort.by("name").descending()));
        List<Movies> data = response.getContent();
        return new ResponsePage(data, response.getTotalPages(), page, size, uri);
    }

    public void updateMovie(@Valid Movies movie) {
        // get persisted instance
        Movies persistedMovie = getMovieById(movie.getId());

        // diff persisted vs request
        JsonNode delta = JsonUtils.getDetailDiffs(movie, persistedMovie);
        if (!delta.isMissingNode()) {
            for (int i = 0; i <= delta.size() - 1; i++) {
                JsonNode movieProperty = delta.path(i);

                Iterator<String> fieldNames = movieProperty.fieldNames();
                while (fieldNames.hasNext()) {
                    try {
                        String key = fieldNames.next();
                        Object value = null;
                        switch (key) {
                            case "name":
                            case "genre":
                                value = movieProperty.path(key).path("after").asText();
                                break;
                            case "yearReleased":
                                value = Integer.valueOf(movieProperty.path(key).path("after").asText());
                                break;
                            case "rating":
                                value = Float.valueOf(movieProperty.path(key).path("after").asText());
                                break;
                            default:
                                MovieManagerException e = new MovieManagerException("Invalid Movie Propery", INVALID_MOVIE_ATTRIBUTE);
                                e.putSubstitution(key);
                                throw e;
                        }

                        new PropertyDescriptor(key, Movies.class).getWriteMethod()
                                .invoke(persistedMovie, value);

                        /*
                        The could have be broken down to be more reader friendly as such:

                        PropertyDescriptor pd = new PropertyDescriptor(key, Movies.class);
                        Method setter = pd.getWriteMethod();
                        setter.invoke(persistedMovie, rating);
                         */

                        movieRepository.saveAndFlush(persistedMovie);
                    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException ex) {
                        LOGGER.error("Error invoking getter reflection [{}]", ex.getMessage(), ex);
                    }
                }
            }
        }
    }

    public void deleteMovie(@NotNull Integer id) {
        movieRepository.deleteById(id);
    }

    private String buildQueryUri(int page, int size) {
        UriComponentsBuilder builder = linkTo(
                methodOn(MovieResource.class).getMovies(page, size))
                .toUriComponentsBuilder();

        int nextPage = ++page;
        builder.replaceQueryParam("page",nextPage);

        return builder.toUriString();
    }
}
