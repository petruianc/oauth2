package com.petru.productservice.service;

import com.petru.productservice.dto.ProductRequest;
import com.petru.productservice.dto.ProductResponse;
import com.petru.productservice.model.Product;
import com.petru.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved!", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products =  productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                                .name(product.getName())
                                .id(product.getId())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .build();
    }
}
