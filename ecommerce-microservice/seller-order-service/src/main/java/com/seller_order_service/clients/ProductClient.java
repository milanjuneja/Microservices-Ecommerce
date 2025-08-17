package com.seller_order_service.clients;

import com.seller_order_service.dto.ProductDTO;
import com.seller_order_service.dto.SellerDTO;
import com.seller_order_service.request.CreateProductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/products/api/{id}")
    ProductDTO getProductById(@PathVariable Long id);

    @GetMapping("/products/seller")
    List<ProductDTO> getProductBySellerId(@RequestParam Long sellerId);

    @PostMapping("/products")
    ProductDTO createProduct(@RequestBody CreateProductRequest request,@RequestParam Long sellerId);
    @DeleteMapping("/products/delete")
    void deleteProduct(Long productId);

    @PutMapping("/products/update")
    ProductDTO updateProduct(@RequestParam Long productId,
                             @RequestBody ProductDTO product);
}