package com.product_service.controller;

import com.product_service.dto.ProductDTO;
import com.product_service.entity.Product;
import com.product_service.exceptions.ProductException;
import com.product_service.request.CreateProductRequest;
import com.product_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {
        return new ResponseEntity<>(productService.findProductById(productId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query){
        return new ResponseEntity<>(productService.searchProducts(query), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(required = false) String category,
                                                        @RequestParam(required = false) String brand,
                                                        @RequestParam(required = false) String color,
                                                        @RequestParam(required = false) String sizes,
                                                        @RequestParam(required = false) Integer minPrice,
                                                        @RequestParam(required = false) Integer maxPrice,
                                                        @RequestParam(required = false) Integer minDiscount,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(required = false) String stock,
                                                        @RequestParam(defaultValue = "0") Integer pageNumber){
        return new ResponseEntity<>(productService.getAllProducts(category, brand, color, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/api/{productId}")
    public ResponseEntity<ProductDTO> getProductByIdForApi(@PathVariable Long productId) throws ProductException {
        Product productById = productService.findProductById(productId);
        return new ResponseEntity<>(ProductDTO.from(productById), HttpStatus.OK);
    }

    @GetMapping("/seller")
    public ResponseEntity<List<ProductDTO>> getProductBySellerId(@RequestParam Long sellerId){
        List<ProductDTO> list = new ArrayList<>();
        List<Product> productBySellerId = productService.getProductBySellerId(sellerId);
        for (Product product: productBySellerId) {
            list.add(ProductDTO.from(product));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductRequest request, @RequestParam Long sellerId){
        Product product = productService.createProduct(request, sellerId);
        return new ResponseEntity<>(ProductDTO.from(product), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProduct(@RequestParam Long productId) throws ProductException {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDTO> updateProduct(@RequestParam Long productId,
                                                    @RequestBody ProductDTO product) throws ProductException {
        ProductDTO dto = new ProductDTO();
        Product product1 = dto.to();
        Product product2 = productService.updateProduct(productId, product1);
        return new ResponseEntity<>(ProductDTO.from(product2), HttpStatus.OK);
    }
    @GetMapping("/debug")
    public String debugPath(HttpServletRequest request) {
        return "Product service received request: " + request.getRequestURI();
    }
}
