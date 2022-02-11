package com.matias.productmicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matias.productmicroservice.entity.CategoryEntity;
import com.matias.productmicroservice.entity.ProductEntity;
import com.matias.productmicroservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> listProduct(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        List<ProductEntity> products = new ArrayList<>();
        if (null == categoryId) {
            products = productService.listAllProduct();
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            products = productService.findByCategory(CategoryEntity.builder().id(categoryId).build());
            if (products.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(products);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductEntity> getProduct(@PathVariable("id") Long id) {
        ProductEntity product = productService.getProduct(id);
        if (null == product) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@Valid @RequestBody ProductEntity product, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        ProductEntity productCreate = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable("id") Long id, @RequestBody ProductEntity product) {
        product.setId(id);
        ProductEntity productDB = productService.updateProduct(product);
        if (productDB == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductEntity> deleteProduct(@PathVariable("id") Long id) {
        ProductEntity productDelete = productService.deleteProduct(id);
        if (productDelete == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDelete);
    }

    @PutMapping(value = "/{id}/stock")
    public ResponseEntity<ProductEntity> updateStockProduct(@PathVariable Long id, @RequestParam(name = "quantity", required = true) Double quantity) {
        ProductEntity product = productService.updateStock(id, quantity);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream().map(err -> {
            Map<String, String> error = new HashMap<>();
            error.put(err.getField(), err.getDefaultMessage());
            return error;

        }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder().code("415").messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
