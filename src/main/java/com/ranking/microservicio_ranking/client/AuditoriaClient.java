package com.ranking.microservicio_ranking.client;

import com.ranking.microservicio_ranking.dto.AuditoriaRequestDTO;
import com.ranking.microservicio_ranking.dto.AuditoriaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-auditoria", path = "/api/auditoria")
public interface AuditoriaClient {

    @PostMapping
    AuditoriaResponseDTO generarAuditoria(@RequestBody AuditoriaRequestDTO auditoria);


}
