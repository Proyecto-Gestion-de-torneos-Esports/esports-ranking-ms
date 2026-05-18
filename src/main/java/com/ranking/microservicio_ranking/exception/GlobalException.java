package com.ranking.microservicio_ranking.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<?> manejoUsuarioNoEncontrado(UsuarioNotFoundException e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado",404);
        error.put("mensaje",e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RankingNotFoundException.class)
    public ResponseEntity<?> manejoNoEncontrado(RankingNotFoundException e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado", 404);
        error.put("mensaje", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejoValidaciones(MethodArgumentNotValidException e){
        HashMap<String, Object> errores = new HashMap<>();
        errores.put("estado",400);
        e.getBindingResult().getFieldErrors().forEach(error-> errores.put(error.getField(),e.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> manejoFeign(FeignException e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado",503);
        error.put("mensaje",e.getMessage());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejoGeneral(Exception e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado",500);
        error.put("mensaje",e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
