package com.matias.shoppingmicroservice.repository;

import com.matias.shoppingmicroservice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    public List<InvoiceEntity> findByCustomerId(Long customerId);

    public InvoiceEntity findByNumberInvoice(String numberInvoice);

}
