package com.order.purchase.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.purchase.builder.RespBuilder;
import com.order.purchase.dto.PurchaseDTO;
import com.order.purchase.entity.Purchase;
import com.order.purchase.response.PurchaseResponse;
import com.order.purchase.response.Resp;
import com.order.purchase.service.PurchaseService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    private static final String AUTH_HEADER = "X-Authorities";
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String ACCESS_DENIED = "ONLY ADMIN CAN PERFORM THIS OPERATION";
    private static final String USER_HEADER = "X-Username";
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/getByUser")
    public Mono<ResponseEntity<Resp>> getPurchaseByUser(
            @RequestParam(required = false) Optional<Integer> productCategory,
            @RequestHeader(AUTH_HEADER) String authorities, @RequestHeader(USER_HEADER) String user) {

        if (!authorities.contains(ADMIN) && !authorities.contains(USER)) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RespBuilder.from(ACCESS_DENIED, HttpStatus.FORBIDDEN).build()));
        }

        return purchaseService.getPurchaseByUser(productCategory, user).collectList()
                .map(purchases -> ResponseEntity.ok().body(RespBuilder.from(purchases, HttpStatus.OK).build()));
    }

    @PostMapping("/order")
    public Mono<ResponseEntity<Resp>> order(@RequestBody PurchaseDTO purchaseRequest,
            @RequestHeader(AUTH_HEADER) String authorities, @RequestHeader(USER_HEADER) String user) {
        if (!authorities.contains(ADMIN) && !authorities.contains(USER)) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RespBuilder.from(ACCESS_DENIED, HttpStatus.FORBIDDEN).build()));
        }
        return purchaseService.order(purchaseRequest, user)
                .map(purchase -> ResponseEntity.ok().body(RespBuilder.from(purchase, HttpStatus.OK).build()));
    }

    @DeleteMapping("/deleteAll")
    public Mono<ResponseEntity<Resp>> deleteAll(@RequestHeader(AUTH_HEADER) String authorities) {
        if (!authorities.contains(ADMIN)) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RespBuilder.from(ACCESS_DENIED, HttpStatus.FORBIDDEN).build()));
        }
        return purchaseService.deleteAll()
                .map(purchase -> ResponseEntity.ok().body(RespBuilder.from(purchase, HttpStatus.OK).build()));
    }

}
