package com.ranking.microservicio_ranking;

import com.ranking.microservicio_ranking.model.Ranking;
import com.ranking.microservicio_ranking.repository.RankingRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Profile("dev")
@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RankingRepository rankingRepository;

    @Override
    public void run(String... args) throws Exception{
        Faker faker = new Faker();
        Random random = new Random();
        Long min = 1L;
        Long max = 1000L;

        for(int i = 0; i<3; i++){
            Ranking ranking = new Ranking();
            ranking.setIdUsuario((long)(i+1));
            ranking.setNombre(faker.artist().name());
            ranking.setPuntaje(ThreadLocalRandom.current().nextLong(min,max+1));
            rankingRepository.save(ranking);
        }
    }

}
