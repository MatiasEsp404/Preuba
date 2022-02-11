package com.matias.shoppingmicroservice.service;

import com.matias.shoppingmicroservice.entity.InvoiceEntity;
import com.matias.shoppingmicroservice.repository.InvoiceItemsRepository;
import com.matias.shoppingmicroservice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemsRepository invoiceItemsRepository;

    @Override
    public List<InvoiceEntity> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public InvoiceEntity createInvoice(InvoiceEntity invoice) {
        InvoiceEntity invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoiceDB != null) {
            return invoiceDB;
        }
        invoice.setState("CREATED");
        return invoiceRepository.save(invoice);
    }

    @Override
    public InvoiceEntity updateInvoice(InvoiceEntity invoice) {
        InvoiceEntity invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null) {
            return null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public InvoiceEntity deleteInvoice(InvoiceEntity invoice) {
        InvoiceEntity invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null) {
            return null;
        }
        invoiceDB.setState("DELETED");
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public InvoiceEntity getInvoice(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }
}
