package com.matias.productmicroservice.service;

import com.matias.productmicroservice.entity.CategoryEntity;
import com.matias.productmicroservice.entity.ProductEntity;
import com.matias.productmicroservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductEntity> listAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public ProductEntity getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public ProductEntity createProduct(ProductEntity product) {
        product.setStatus("CREATED");
        product.setCreateAt(new Date());

        return productRepository.save(product);
    }

    @Override
    public ProductEntity updateProduct(ProductEntity product) {
        ProductEntity productDB = getProduct(product.getId());
        if (null == productDB) {
            return null;
        }
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        productDB.setCategory(product.getCategory());
        productDB.setPrice(product.getPrice());
        return productRepository.save(productDB);
    }

    @Override
    public ProductEntity deleteProduct(Long id) {
        ProductEntity productDB = getProduct(id);
        if (null == productDB) {
            return null;
        }
        productDB.setStatus("DELETED");
        return productRepository.save(productDB);
    }

    @Override
    public List<ProductEntity> findByCategory(CategoryEntity category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public ProductEntity updateStock(Long id, Double quantity) {
        ProductEntity productDB = getProduct(id);
        if (null == productDB) {
            return null;
        }
        Double stock = productDB.getStock() + quantity;
        productDB.setStock(stock);
        return productRepository.save(productDB);
    }
}
