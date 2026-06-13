package com.ranking.microservicio_ranking.repository;

import com.ranking.microservicio_ranking.model.Ranking;
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

    @Test
    void testOrdenarRanking(){
        Ranking ranking1 = new Ranking();
        ranking1.setIdUsuario(1L);
        ranking1.setNombre("Usuario1");
        ranking1.setPuntaje(100L);

        Ranking ranking2 = new Ranking();
        ranking2.setIdUsuario(2L);
        ranking2.setNombre("Usuario2");
        ranking2.setPuntaje(200L);

        rankingRepository.save(ranking1);
        rankingRepository.save(ranking2);

        List<Ranking> rankings = rankingRepository.ordenarPuntajeMayoraMenor();

        assertNotNull(rankings);
        assertEquals(2, rankings.size());
    }
}
