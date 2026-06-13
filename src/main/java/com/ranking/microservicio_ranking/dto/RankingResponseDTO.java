package com.ranking.microservicio_ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingResponseDTO extends RepresentationModel<RankingResponseDTO> {

    private Long idUsuario;
    private String nombre;
    private Long puntaje;
}
