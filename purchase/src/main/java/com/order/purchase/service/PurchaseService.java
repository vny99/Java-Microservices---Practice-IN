package com.order.purchase.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.purchase.builder.PurchaseBuilder;
import com.order.purchase.builder.PurchaseResponseBuilder;
import com.order.purchase.dao.PurchaseDAO;
import com.order.purchase.dto.PurchaseDTO;
import com.order.purchase.entity.Product;
import com.order.purchase.entity.Purchase;
import com.order.purchase.entity.Stock;
import com.order.purchase.exception.OutOfStockException;
import com.order.purchase.response.PurchaseResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseDAO purchaseDAO;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String PRODUCT_API_URL = "http://localhost:8081/alianz/product";

    public Flux<PurchaseResponse> getPurchaseByUser(Optional<Integer> productCategory, String user) {
        return getPurchasesByUserName(user).collectList().flatMapMany(purchases -> {
            return buildPurchaseResponses(purchases);
        }).collectList().flatMapMany(responses -> filterPurchase(responses, productCategory)).collectList()
                .flatMapMany(this::filterEmptyResponses);
    }

    private Flux<PurchaseResponse> buildPurchaseResponses(List<Purchase> purchasesList) {
        return Flux.fromIterable(purchasesList).flatMap(purchase -> {
            return getProductByIds(convertProductIdsBackToNormal(purchase.getProductIds())).collectList()
                    .flatMap(products -> {
                        return Mono.just(PurchaseResponseBuilder.from(purchase, products).build());
                    });
        });
    }

    private Flux<PurchaseResponse> filterEmptyResponses(List<PurchaseResponse> responses) {
        return Flux.fromIterable(responses).filter(response -> !response.getProducts().isEmpty());
    }

    private Flux<PurchaseResponse> filterPurchase(List<PurchaseResponse> responses, Optional<Integer> productCategory) {
        return Mono.justOrEmpty(productCategory).flatMapMany(type -> {
            return Flux.fromIterable(responses).flatMap(response -> {
                List<Product> products = response.getProducts();
                response.setProducts(
                        products.stream().filter(product -> product.getProductCategoryId() == type).toList());
                return Mono.just(response);
            });
        }).switchIfEmpty(Flux.fromIterable(responses));
    }

    public Flux<Purchase> getPurchasesByUserName(String user) {
        return Mono.justOrEmpty(user).flatMapMany(purchaseDAO::findByUserName);
    }

    public Mono<Purchase> order(PurchaseDTO purchaseDTO, String user) {
        return getProductByIds(purchaseDTO.getProductIds()).collectList().flatMap(products -> {
            return Mono.justOrEmpty(user).flatMap(userName -> {
                return getQuantityMap(products).flatMap(quantityMap -> {
                    return buildPurchase(purchaseDTO, products, quantityMap, userName).doOnSuccess(purchase -> {
                        updateStock(quantityMap).subscribe();
                    }).onErrorMap(ex -> new OutOfStockException(ex.getMessage()));
                });
            }).flatMap(purchaseDAO::savePurchase);
        });
    }

    private Flux<Product> getProductByIds(List<String> ids) {
        return webClientBuilder.build().get()
                .uri(PRODUCT_API_URL, uriBuilder -> uriBuilder.path("/getByIds").queryParam("ids", ids).build())
                .retrieve().bodyToFlux(Product.class);
    }

    private Mono<Map<String, Integer>> getQuantityMap(List<Product> products) {
        return Flux.fromIterable(products).map(Product::getName).collectList().flatMap(productNames -> {
            return webClientBuilder.build().get()
                    .uri(PRODUCT_API_URL, uriBuilder -> uriBuilder.path("/stock/getByNames")
                            .queryParam("productNames", productNames).build())
                    .retrieve().bodyToFlux(Stock.class)
                    .collectMap(Stock::getProductName, Stock::getProductStock);
        });
    }

    private Mono<Purchase> buildPurchase(PurchaseDTO purchaseDTO, List<Product> products,
            Map<String, Integer> quantityMap, String userName) {
        for (Product product : products) {
            if (quantityMap.containsKey(product.getName()) && quantityMap.get(product.getName()) > 0) {
                quantityMap.put(product.getName(), quantityMap.get(product.getName()) - 1);
            } else {
                throw new OutOfStockException("Product out of stock " + product.getName());
            }
        }
        return Mono.just(PurchaseBuilder.from(purchaseDTO, userName, LocalDate.now(),
                products.stream().map(Product::getPrice).mapToDouble(Double::doubleValue).sum(), products.size())
                .build());
    }

    private Mono<Void> updateStock(Map<String, Integer> quantityMap) {
        return Flux.fromIterable(quantityMap.entrySet()).flatMap(entry -> {
            return webClientBuilder.build().put()
                    .uri(PRODUCT_API_URL, uriBuilder -> uriBuilder.path("/stock/update")
                            .queryParam("productName", entry.getKey()).queryParam("stock", entry.getValue()).build())
                    .retrieve().bodyToMono(Void.class);
        }).then();
    }

    private List<String> convertProductIdsBackToNormal(List<String> productIds) {
        return productIds.stream().map(id -> id.replace("[", "")).map(id -> id.replace("]", "")).toList();
    }

    public Mono<Void> deleteAll() {
        return purchaseDAO.deleteAll();
    }

}
