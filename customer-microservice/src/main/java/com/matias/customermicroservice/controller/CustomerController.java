package com.matias.customermicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matias.customermicroservice.entity.CustomerEntity;
import com.matias.customermicroservice.entity.RegionEntity;
import com.matias.customermicroservice.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    // -------------------Retrieve All Customers--------------------------------------------

    @GetMapping
    public ResponseEntity<List<CustomerEntity>> listAllCustomers(@RequestParam(name = "regionId", required = false) Long regionId) {
        List<CustomerEntity> customers = new ArrayList<>();
        if (null == regionId) {
            customers = customerService.findCustomerAll();
            if (customers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            RegionEntity region = new RegionEntity();
            region.setId(regionId);
            customers = customerService.findCustomersByRegion(region);
            if (null == customers) {
                log.error("Customers with Region id {} not found.", regionId);
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(customers);
    }

    // -------------------Retrieve Single Customer------------------------------------------

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerEntity> getCustomer(@PathVariable("id") long id) {
        log.info("Fetching Customer with id {}", id);
        CustomerEntity customer = customerService.getCustomer(id);
        if (null == customer) {
            log.error("Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    // -------------------Create a Customer-------------------------------------------

    @PostMapping
    public ResponseEntity<CustomerEntity> createCustomer(@Valid @RequestBody CustomerEntity customer, BindingResult result) {
        log.info("Creating Customer : {}", customer);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }

        CustomerEntity customerDB = customerService.createCustomer(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
    }

    // ------------------- Update a Customer ------------------------------------------------

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") long id, @RequestBody CustomerEntity customer) {
        log.info("Updating Customer with id {}", id);

        CustomerEntity currentCustomer = customerService.getCustomer(id);

        if (null == currentCustomer) {
            log.error("Unable to update. Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        currentCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(currentCustomer);
    }

    // ------------------- Delete a Customer-----------------------------------------

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomerEntity> deleteCustomer(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Customer with id {}", id);

        CustomerEntity customer = customerService.getCustomer(id);
        if (null == customer) {
            log.error("Unable to delete. Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        customer = customerService.deleteCustomer(customer);
        return ResponseEntity.ok(customer);
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
