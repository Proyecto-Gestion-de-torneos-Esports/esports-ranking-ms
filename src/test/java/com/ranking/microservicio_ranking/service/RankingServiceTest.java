package com.ranking.microservicio_ranking.service;

import com.ranking.microservicio_ranking.client.AuditoriaClient;
import com.ranking.microservicio_ranking.client.EstadisticaClient;
import com.ranking.microservicio_ranking.client.UsuarioClient;
import com.ranking.microservicio_ranking.dto.AuditoriaRequestDTO;
import com.ranking.microservicio_ranking.dto.EstadisticaResponseDTO;
import com.ranking.microservicio_ranking.dto.RankingResponseDTO;
import com.ranking.microservicio_ranking.dto.UsuarioResponseDTO;
import com.ranking.microservicio_ranking.model.Ranking;
import com.ranking.microservicio_ranking.repository.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private EstadisticaClient estadisticaClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private AuditoriaClient auditoriaClient;

    @Mock
    private UsuarioResponseDTO usuarioResponseDTO;

    @InjectMocks
    private RankingService rankingService;

    private Ranking ranking;
    private EstadisticaResponseDTO estadistica;
    private UsuarioResponseDTO usuario;

    @BeforeEach
    void setUp(){
        ranking = new Ranking();
        ranking.setIdUsuario(1L);
        ranking.setNombre("John Doe");
        ranking.setPuntaje(100L);

        usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setNombreUsuario("NombreUsuario");
        usuario.setCorreo("nombre.usuario@correo.com");
        usuario.setEquipoId(1L);
        usuario.setActivo(true);

        estadistica = new EstadisticaResponseDTO();
        estadistica.setId(1L);
        estadistica.setPartidaId(1L);
        estadistica.setUsuarioId(usuario.getId());
        estadistica.setMetrica("Goles");
        estadistica.setValor(10);
        estadistica.setActivo(true);
    }
    @Test
    void testObtenerTodo(){
        when(rankingRepository.findAll()).thenReturn(List.of(ranking));

        List<RankingResponseDTO> rankings = rankingService.obtenerTodo();

        assertNotNull(rankings);
        assertEquals(1, rankings.size());
    }

    @Test
    void testBuscarPorId(){
        when(rankingRepository.findById(1L)).thenReturn(Optional.of(ranking));

        RankingResponseDTO ranking1 = rankingService.buscarPorId(1L);

        assertEquals(ranking.getIdUsuario(), ranking1.getIdUsuario());

        verify(rankingRepository).findById(1L);
    }

    @Test
    void testGuardarRanking(){
        when(estadisticaClient.obtenerTodos()).thenReturn(List.of(estadistica));
        when((usuarioClient.buscarPorId(1L))).thenReturn(usuario);

        rankingService.guardarRanking(1L);

        verify(rankingRepository).save(any(Ranking.class));
        verify(auditoriaClient).generarAuditoria(any(AuditoriaRequestDTO.class));
    }

    @Test
    void testOrdenarPorPuntaje(){
        when(rankingRepository.ordenarPuntajeMayoraMenor()).thenReturn(List.of(ranking));

        List<RankingResponseDTO> rankings = rankingService.rankingOrdenaPorPuntaje();

        assertNotNull(rankings);
        assertEquals(1,rankings.size());
    }

}
