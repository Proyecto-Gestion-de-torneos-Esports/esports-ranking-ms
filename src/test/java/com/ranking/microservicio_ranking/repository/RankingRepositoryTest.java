package com.ranking.microservicio_ranking.repository;

import com.ranking.microservicio_ranking.model.Ranking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RankingRepositoryTest {

    @Autowired
    private RankingRepository rankingRepository;

    @BeforeEach
    void setUp() {
        rankingRepository.deleteAll();
    }

    @Test
    void testOrdenarRanking() {

        Ranking ranking1 = new Ranking();
        ranking1.setIdUsuario(1L);
        ranking1.setNombre("Usuario1");
        ranking1.setPuntaje(100L);

        Ranking ranking2 = new Ranking();
        ranking2.setIdUsuario(2L);
        ranking2.setNombre("Usuario2");
        ranking2.setPuntaje(200L);

        Ranking ranking3 = new Ranking();
        ranking3.setIdUsuario(3L);
        ranking3.setNombre("Usuario3");
        ranking3.setPuntaje(150L);

        rankingRepository.save(ranking1);
        rankingRepository.save(ranking2);
        rankingRepository.save(ranking3);


        List<Ranking> rankings = rankingRepository.ordenarPuntajeMayoraMenor();


        assertNotNull(rankings);
        assertEquals(3, rankings.size());

        assertEquals(200L, rankings.get(0).getPuntaje(), "El primer lugar debe tener 200 puntos");
        assertEquals(150L, rankings.get(1).getPuntaje(), "El segundo lugar debe tener 150 puntos");
        assertEquals(100L, rankings.get(2).getPuntaje(), "El tercer lugar debe tener 100 puntos");
    }
}