package com.matias.shoppingmicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matias.shoppingmicroservice.entity.InvoiceEntity;
import com.matias.shoppingmicroservice.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/invoices")
public class InvoiceController {


    @Autowired
    InvoiceService invoiceService;

    // -------------------Retrieve All Invoices--------------------------------------------
    @GetMapping
    public ResponseEntity<List<InvoiceEntity>> listAllInvoices() {
        List<InvoiceEntity> invoices = invoiceService.findInvoiceAll();
        if (invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }

    // -------------------Retrieve Single Invoice------------------------------------------
    @GetMapping(value = "/{id}")
    public ResponseEntity<InvoiceEntity> getInvoice(@PathVariable("id") long id) {
        log.info("Fetching Invoice with id {}", id);
        InvoiceEntity invoice = invoiceService.getInvoice(id);
        if (null == invoice) {
            log.error("Invoice with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    // -------------------Create a Invoice-------------------------------------------
    @PostMapping
    public ResponseEntity<InvoiceEntity> createInvoice(@Valid @RequestBody InvoiceEntity invoice, BindingResult result) {
        log.info("Creating Invoice : {}", invoice);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        InvoiceEntity invoiceDB = invoiceService.createInvoice(invoice);

        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDB);
    }

    // ------------------- Update a Invoice ------------------------------------------------
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable("id") long id, @RequestBody InvoiceEntity invoice) {
        log.info("Updating Invoice with id {}", id);

        invoice.setId(id);
        InvoiceEntity currentInvoice = invoiceService.updateInvoice(invoice);

        if (currentInvoice == null) {
            log.error("Unable to update. Invoice with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currentInvoice);
    }

    // ------------------- Delete a Invoice-----------------------------------------
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<InvoiceEntity> deleteInvoice(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Invoice with id {}", id);

        InvoiceEntity invoice = invoiceService.getInvoice(id);
        if (invoice == null) {
            log.error("Unable to delete. Invoice with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        invoice = invoiceService.deleteInvoice(invoice);
        return ResponseEntity.ok(invoice);
    }

    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream().map(err -> {
            Map<String, String> error = new HashMap<>();
            error.put(err.getField(), err.getDefaultMessage());
            return error;

        }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder().code("01").messages(errors).build();
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
