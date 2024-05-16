package com.account.account.advicer;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.account.account.builder.RespBuilder;
import com.account.account.exceptions.UserAlreadyExistsException;
import com.account.account.response.Resp;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class CustomExceptionHandler {



    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Mono<ResponseEntity<Resp>> handleProductNotFound(NoSuchElementException exception){
       return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RespBuilder.from(exception.getMessage(), HttpStatus.NOT_FOUND).build()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Resp>> handleIllegalArgumentException(IllegalArgumentException exception){
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RespBuilder.from(exception.getMessage(), HttpStatus.BAD_REQUEST).build())); 
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Mono<ResponseEntity<Resp>> handleUserAlreadyExistsException(UserAlreadyExistsException exception){
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RespBuilder.from(exception.getMessage(), HttpStatus.BAD_REQUEST).build()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Mono<ResponseEntity<Resp>> handleUsernameNotFoundException(UsernameNotFoundException exception){
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RespBuilder.from(exception.getMessage(), HttpStatus.BAD_REQUEST).build()));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<Resp>> handleBadCredentialsException(BadCredentialsException exception){
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(RespBuilder.from(exception.getMessage(), HttpStatus.FORBIDDEN).build()));
    }

}
