package com.ranking.microservicio_ranking.assembler;

import com.ranking.microservicio_ranking.controller.RankingController;
import com.ranking.microservicio_ranking.dto.RankingResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RankingModelAssembler implements RepresentationModelAssembler<RankingResponseDTO, RankingResponseDTO> {

    @Override
    public RankingResponseDTO toModel(RankingResponseDTO ranking){

        ranking.add(linkTo(methodOn(RankingController.class).obtenerTodo()).withRel("rankings"));
        ranking.add(linkTo(methodOn(RankingController.class).buscarPorId(ranking.getIdUsuario())).withSelfRel());
        return ranking;
    }
}
