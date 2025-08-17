package com.cart_service.clients;

import com.cart_service.dto.ProductDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "product-service")
public interface ProductClient {
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductByIdFallback")
    @Retry(name = "productService")
    @GetMapping("/products/api/{id}")
    ProductDTO getProductById(@PathVariable Long id);

    default ProductDTO getProductByIdFallback(Long id, Throwable t) {
        ProductDTO fallbackProduct = new ProductDTO();
        fallbackProduct.setId(id);
        fallbackProduct.setTitle("Fallback product");
        fallbackProduct.setDescription("This is a fallback due to: " + t.getMessage());
        fallbackProduct.setSellingPrice(0);
        return fallbackProduct;
    }
}

