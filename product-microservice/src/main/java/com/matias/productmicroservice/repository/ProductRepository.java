package com.matias.productmicroservice.repository;

import com.matias.productmicroservice.entity.CategoryEntity;
import com.matias.productmicroservice.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    public List<ProductEntity> findByCategory(CategoryEntity category);

}
