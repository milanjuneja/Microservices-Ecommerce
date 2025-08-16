package com.seller_order_service.controller;

import com.seller_order_service.clients.ProductClient;
import com.seller_order_service.clients.SellerClient;
import com.seller_order_service.dto.ProductDTO;
import com.seller_order_service.dto.SellerDTO;
import com.seller_order_service.request.CreateProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers/products")
@AllArgsConstructor
public class SellerProductController {

    private final SellerClient sellerClient;

    private final ProductClient productClient;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProductBySellerId(
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        SellerDTO seller = sellerClient.findSellerByEmail(email);
        return new ResponseEntity<>(productClient.getProductBySellerId(seller.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductRequest request,
                                                    @RequestHeader("X-User-Email") String email) throws Exception {
        SellerDTO seller = sellerClient.findSellerByEmail(email);
        return new ResponseEntity<>(productClient.createProduct(request, seller.getId()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productClient.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                 @RequestBody ProductDTO product){
        return new ResponseEntity<>(productClient.updateProduct(productId, product), HttpStatus.OK);
    }

}