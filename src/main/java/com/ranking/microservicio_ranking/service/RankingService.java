package com.ranking.microservicio_ranking.service;

import com.ranking.microservicio_ranking.client.AuditoriaClient;
import com.ranking.microservicio_ranking.client.EstadisticaClient;
import com.ranking.microservicio_ranking.client.UsuarioClient;
import com.ranking.microservicio_ranking.dto.AuditoriaRequestDTO;
import com.ranking.microservicio_ranking.dto.EstadisticaResponseDTO;
import com.ranking.microservicio_ranking.dto.RankingResponseDTO;
import com.ranking.microservicio_ranking.dto.UsuarioResponseDTO;
import com.ranking.microservicio_ranking.exception.RankingNotFoundException;
import com.ranking.microservicio_ranking.exception.UsuarioNotFoundException;
import com.ranking.microservicio_ranking.model.Ranking;
import com.ranking.microservicio_ranking.repository.RankingRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final AuditoriaClient auditoriaClient;
    private final EstadisticaClient estadisticaClient;
    private final UsuarioClient usuarioClient;

    public RankingResponseDTO mapToDTO(Ranking ranking){
        return new RankingResponseDTO(
                ranking.getIdUsuario(),
                ranking.getNombre(),
                ranking.getPuntaje()
        );
    }
    public List<RankingResponseDTO> obtenerTodo(){
        return rankingRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RankingResponseDTO buscarPorId(Long id){
        Optional<Ranking> ranking = rankingRepository.findById(id);
        if(ranking.isPresent()){
            log.info("Ranking encontrado con id {}", id);
            return mapToDTO(ranking.orElseThrow());
        }
        log.warn("Ranking con id {} no fue encontrado", id);
        throw new RankingNotFoundException("El ranking con id "+id+" no fue encontrado");
    }

    @Transactional
    public void guardarRanking(Long idUsuario){
        List<EstadisticaResponseDTO> estadisticas = estadisticaClient.obtenerTodos();
        UsuarioResponseDTO usuario = validarUsuarioExiste(idUsuario);
        Ranking ranking = new Ranking();
        Long puntaje = 0L;

        for(EstadisticaResponseDTO estadistica: estadisticas){
            if(estadistica.getUsuarioId().equals(idUsuario)){
                ranking.setIdUsuario(estadistica.getUsuarioId());
                ranking.setNombre(usuario.getNombreUsuario());
                if(estadistica.getMetrica().equalsIgnoreCase("Goles") || estadistica.getMetrica().equalsIgnoreCase("Kills")){
                    puntaje+=(estadistica.getValor()*5L);
                }else if(estadistica.getMetrica().equalsIgnoreCase("Asistencias")){
                    puntaje+=(estadistica.getValor()*3L);
                }else if(estadistica.getMetrica().equalsIgnoreCase("Muertes")){
                    puntaje-=(estadistica.getValor());
                }

                ranking.setPuntaje(puntaje);
            }
        }
        rankingRepository.save(ranking);
        log.info("Ranking generado con exito!");
        generarAuditoria("Se agrego una fila en ranking");
    }

    public List<RankingResponseDTO> rankingOrdenaPorPuntaje(){
        return rankingRepository.ordenarPuntajeMayoraMenor()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO validarUsuarioExiste(Long idUsuario){
        try{
            UsuarioResponseDTO usuario = usuarioClient.buscarPorId(idUsuario);
            log.info("Usuario con id {} encontrado",idUsuario);
            return usuario;
        }catch (FeignException.NotFound e){
            log.warn("Usuario con id {} no fue encontrado",idUsuario);
            throw new UsuarioNotFoundException("El usuario con id "+idUsuario+" no fue encontrado");
        }
    }

    @Transactional
    public void generarAuditoria(String detalle){
        AuditoriaRequestDTO dto = new AuditoriaRequestDTO();
        LocalDate ahora = LocalDate.now();
        dto.setDetalle(detalle);
        dto.setFecha(ahora);
        auditoriaClient.generarAuditoria(dto);
        log.info("Auditoria generada");
    }
}
