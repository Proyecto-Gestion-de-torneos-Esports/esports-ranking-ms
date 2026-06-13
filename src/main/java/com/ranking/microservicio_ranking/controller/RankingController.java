package com.ranking.microservicio_ranking.controller;

import com.ranking.microservicio_ranking.assembler.RankingModelAssembler;
import com.ranking.microservicio_ranking.dto.RankingResponseDTO;
import com.ranking.microservicio_ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "Operaciones relacionadas con los rankings")
public class RankingController {

    private final RankingService rankingService;
    private final RankingModelAssembler rankingAssembler;

    @GetMapping
    @Operation(summary = "Listar rankings", description = "Consulta de rankings disponibles")
    public ResponseEntity<?> obtenerTodo(){
        List<RankingResponseDTO> rankings = rankingService.obtenerTodo()
                .stream().map(rankingAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<RankingResponseDTO> collectionModel =  CollectionModel.of(rankings, linkTo(methodOn(RankingController.class).obtenerTodo()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busqueda de rankings por su ID", description = "Consulta de un ranking en especifico")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        RankingResponseDTO ranking = rankingService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(rankingAssembler.toModel(ranking));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Generar un ranking", description = "Registro de un ranking para un usuario")
    public ResponseEntity<?> guardarRanking(@PathVariable Long id){
        rankingService.guardarRanking(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/puntaje")
    @Operation(summary = "Listar rankings ordenados por puntaje", description = "Consulta de rankings ordenados por puntaje")
    public ResponseEntity<?> listarOrdenado(){
        List<RankingResponseDTO> rankings = rankingService.rankingOrdenaPorPuntaje()
                .stream().map(rankingAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<RankingResponseDTO> collectionModel =  CollectionModel.of(rankings, linkTo(methodOn(RankingController.class).obtenerTodo()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }
}
