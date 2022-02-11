package com.matias.shoppingmicroservice.service;

import com.matias.shoppingmicroservice.entity.InvoiceEntity;

import java.util.List;

public interface InvoiceService {
    public List<InvoiceEntity> findInvoiceAll();

    public InvoiceEntity createInvoice(InvoiceEntity invoice);

    public InvoiceEntity updateInvoice(InvoiceEntity invoice);

    public InvoiceEntity deleteInvoice(InvoiceEntity invoice);

    public InvoiceEntity getInvoice(Long id);
}
