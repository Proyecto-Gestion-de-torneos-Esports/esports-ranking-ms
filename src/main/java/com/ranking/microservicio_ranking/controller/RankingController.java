package com.ranking.microservicio_ranking.controller;

import com.ranking.microservicio_ranking.dto.RankingResponseDTO;
import com.ranking.microservicio_ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<?> obtenerTodo(){
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.obtenerTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.buscarPorId(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> guardarRanking(@PathVariable Long id){
        rankingService.guardarRanking(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/puntaje")
    public ResponseEntity<?> listarOrdenado(){
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.rankingOrdenaPorPuntaje());
    }
}
