package com.ranking.microservicio_ranking.controller;

import com.ranking.microservicio_ranking.dto.RankingResponseDTO;
import com.ranking.microservicio_ranking.model.Ranking;
import com.ranking.microservicio_ranking.service.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RankingService rankingService;

    private RankingResponseDTO ranking;

    @BeforeEach
    void setUp(){
        ranking = new RankingResponseDTO();
        ranking.setIdUsuario(1L);
        ranking.setNombre("NombreUsuario");
        ranking.setPuntaje(50L);
    }

    @Test
    void testObtenerTodos() throws Exception{
        when(rankingService.obtenerTodo()).thenReturn(List.of(ranking));

        mockMvc.perform(get("/api/ranking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("NombreUsuario"))
                .andExpect(jsonPath("$[0].puntaje").value(50L));

        List<RankingResponseDTO> rankings = rankingService.obtenerTodo();

        assertNotNull(rankings);
        assertEquals(1, rankings.size());
    }

    @Test
    void testBuscarPorId() throws Exception{
        when(rankingService.buscarPorId(1L)).thenReturn(ranking);

        mockMvc.perform(get("/api/ranking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nombre").value("NombreUsuario"))
                .andExpect(jsonPath("$.puntaje").value(50L));

        verify(rankingService).buscarPorId(1L);
    }

    @Test
    void testGuardarRanking() throws Exception{
        doNothing().when(rankingService).guardarRanking(1L);

        mockMvc.perform(post("/api/ranking/1"))
                .andExpect(status().isCreated());

        verify(rankingService).guardarRanking(1L);
    }

    @Test
    void testOrdenarPorPuntaje() throws Exception{
        when(rankingService.rankingOrdenaPorPuntaje()).thenReturn(List.of(ranking));

        mockMvc.perform(get("/api/ranking/puntaje"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("NombreUsuario"))
                .andExpect(jsonPath("$[0].puntaje").value(50L));

        List<RankingResponseDTO> rankings = rankingService.rankingOrdenaPorPuntaje();

        assertNotNull(rankings);
        assertEquals(1, rankings.size());
    }

}
