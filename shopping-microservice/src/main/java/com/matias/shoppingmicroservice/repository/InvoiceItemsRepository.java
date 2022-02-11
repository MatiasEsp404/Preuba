package com.matias.shoppingmicroservice.repository;

import com.matias.shoppingmicroservice.entity.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemsRepository extends JpaRepository<InvoiceItemEntity, Long> {

}
