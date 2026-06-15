package com.ranking.microservicio_ranking.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranking.microservicio_ranking.assembler.RankingModelAssembler;
import com.ranking.microservicio_ranking.dto.RankingResponseDTO;
import com.ranking.microservicio_ranking.service.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(RankingController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "ADMIN")
@Import(RankingModelAssembler.class)
public class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RankingService rankingService;

    private RankingResponseDTO ranking;

    @BeforeEach
    void setUp() {
        ranking = new RankingResponseDTO();
        ranking.setIdUsuario(1L);
        ranking.setNombre("NombreUsuario");
        ranking.setPuntaje(50L);
    }

    @Test
    void testObtenerTodos() throws Exception {
        when(rankingService.obtenerTodo()).thenReturn(List.of(ranking));

        mockMvc.perform(get("/api/ranking")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(rankingService).obtenerTodo();
    }

    @Test
    void testBuscarPorId() throws Exception {
        when(rankingService.buscarPorId(1L)).thenReturn(ranking);

        mockMvc.perform(get("/api/ranking/1")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nombre").value("NombreUsuario"))
                .andExpect(jsonPath("$.puntaje").value(50L))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(rankingService).buscarPorId(1L);
    }

    @Test
    void testGuardarRanking() throws Exception {
        doNothing().when(rankingService).guardarRanking(1L);

        mockMvc.perform(post("/api/ranking/1")
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(rankingService).guardarRanking(1L);
    }

    @Test
    void testOrdenarPorPuntaje() throws Exception {
        when(rankingService.rankingOrdenaPorPuntaje()).thenReturn(List.of(ranking));

        mockMvc.perform(get("/api/ranking/puntaje")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(rankingService).rankingOrdenaPorPuntaje();
    }
}