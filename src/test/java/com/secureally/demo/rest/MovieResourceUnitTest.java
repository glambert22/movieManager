package com.secureally.demo.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.secureally.demo.dao.entities.Movies;
import com.secureally.demo.service.MovieService;
import com.secureally.demo.utils.ResponsePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Tag("Unit")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Transactional
class MovieResourceUnitTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock(lenient = true)
    private MovieService mockMovieService;

    @InjectMocks
    private MovieResource systemUnderTest;

    private MockMvc mockMvc;

    private JsonNode testData;
    private Movies verificationMovie;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) throws IOException {
        initMocks(this);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        testData = MAPPER.readTree(Resources.getResource(this.getClass().getSimpleName() + ".json"));
        verificationMovie = MAPPER.readValue(testData.path("CREATE").toString(), Movies.class);

        when(mockMovieService.createMoviesEntry(any(Movies.class))).thenReturn(verificationMovie);
    }

    @Test
    void testMovieCreate() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/api/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testData.path("CREATE").toString()))
                .andExpect(header().exists("Location"))
                .andExpect(status().isCreated()).andReturn();

        ResponsePage response = MAPPER.readValue(result.getResponse().getContentAsString(), ResponsePage.class);

        assertNotNull(response);
        JsonNode responseContent = MAPPER.valueToTree(response.getData().getEntity());
        assertTrue(responseContent.has("id"));
    }

    @Test
    void testMovieList() throws Exception {

        MvcResult result = this.mockMvc.perform(get("/api/movie/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode response = MAPPER.readValue(result.getResponse().getContentAsString(), JsonNode.class);

        assertNotNull(response);
        assertTrue(response.path("data").has("entities"));
        assertTrue(response.path("_meta").path("numberOfElements").intValue() > 0);
    }


//    @Test
    void testMovieThrowsConstraintViolation() throws Exception {

        when(mockMovieService.createMoviesEntry(any(Movies.class))).thenThrow(new DataIntegrityViolationException("Duplicate"));

       this.mockMvc.perform(post("/api/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testData.path("CREATE").toString()))
                .andExpect(status().isBadRequest());
    }

}