package com.product_service.service.impl;

import com.product_service.entity.Product;
import com.product_service.exceptions.ProductException;
import com.product_service.repo.ProductRepository;
import com.product_service.request.CreateProductRequest;
import com.product_service.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Long sellerId) {
        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setDescription(req.getDescription());
        product.setMrpPrice(req.getMrpPrice());
        product.setSellingPrice(req.getSellingPrice());
        product.setQuantity(req.getQuantity());
        product.setColor(req.getColor());
        product.setImages(req.getImages());
        product.setNumOfRatings(0);
        product.setCategoryId(req.getCategoryId());
        product.setSellerId(req.getSellerId());
        product.setCreatedAt(LocalDate.now());
        product.setSizes(req.getSizes());
        product.setDiscountPercentage(discountPercentage);
        product.setSellerId(sellerId);

        return productRepository.save(product);
    }
    private int calculateDiscountPercentage(double mrpPrice, double sellingPrice){
        if(mrpPrice < 0)
            throw new IllegalArgumentException("Actual price must be greater than zero ");
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice) * 100;
        return (int) discountPercentage;
    }



    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);

        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductException("Product not found with id -> " + productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        //return productRepository.searchProduct(query);
        return null;
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String color, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(category != null) {
                //Join<Product, Category> categoryJoin = root.join("category");
                //predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"),category));
            }
            if(brand != null && !brand.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("brand"),brand));
            if(color != null && !color.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("color"),color));
            if(sizes != null && !sizes.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("sizes"),sizes));
            if(minPrice != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"),minPrice));
            if(maxPrice != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"),maxPrice));
            if(minDiscount != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"),minDiscount));
            if(stock != null)
                predicates.add(criteriaBuilder.equal(root.get("stock"),stock));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));

        });

        Pageable pageable;
        if(sort != null && !sort.isEmpty()){
            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };

        }
        else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber:0, 10, Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
