package com.matias.productmicroservice.service;

import com.matias.productmicroservice.entity.CategoryEntity;
import com.matias.productmicroservice.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    public List<ProductEntity> listAllProduct();

    public ProductEntity getProduct(Long id);

    public ProductEntity createProduct(ProductEntity product);

    public ProductEntity updateProduct(ProductEntity product);

    public ProductEntity deleteProduct(Long id);

    public List<ProductEntity> findByCategory(CategoryEntity category);

    public ProductEntity updateStock(Long id, Double quantity);
}
