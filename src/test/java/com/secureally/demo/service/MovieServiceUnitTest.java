package com.secureally.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.secureally.demo.dao.entities.Movies;
import com.secureally.demo.dao.repository.MovieRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Tag("Unit")
@ExtendWith(MockitoExtension.class)
class MovieServiceUnitTest {

    @InjectMocks
    private MovieService systemUnderTest;

    @Mock
    private MovieRepository mockMovieRepository;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static Movies testData;

    @BeforeAll
    public static void init() throws IOException {
        testData = objectMapper.readValue(Resources.getResource(MovieServiceUnitTest.class.getSimpleName() + ".json"), Movies.class);
    }

    @BeforeEach
    public void setUp() {

    }


    @Test
    public void testShouldPeristMovie() {
        // given
        Movies deepCopy = (Movies) SerializationUtils.clone(testData);
        deepCopy.setId(4);
        given(mockMovieRepository.saveAndFlush(any(Movies.class))).willReturn(deepCopy);

        // when
        systemUnderTest.createMoviesEntry(testData);

        // then
        then(mockMovieRepository).should(times(1)).saveAndFlush(testData);
    }

    @Test
    public void testShouldGetMovieById() {
        // given
        testData.setId(5);
        given(mockMovieRepository.findById(anyInt())).willReturn(Optional.of(testData));

        // when
        Movies response = systemUnderTest.getMovieById(5);

        // then
        assertEquals(response, testData);
    }

    @Test
    public void testShouldDataIntegrityViolationException(){
        // given
        given(mockMovieRepository.saveAndFlush(any(Movies.class))).willThrow(new DataIntegrityViolationException("Constraint violated"));

        // when
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> systemUnderTest.createMoviesEntry(testData),
                "Exception exception = ");
        assertEquals("Constraint violated", exception.getMessage());
    }

}