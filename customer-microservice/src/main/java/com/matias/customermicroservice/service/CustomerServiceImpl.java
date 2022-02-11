package com.matias.customermicroservice.service;

import com.matias.customermicroservice.entity.CustomerEntity;
import com.matias.customermicroservice.entity.RegionEntity;
import com.matias.customermicroservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{


    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<CustomerEntity> findCustomerAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<CustomerEntity> findCustomersByRegion(RegionEntity region) {
        return customerRepository.findByRegion(region);
    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity customer) {

        CustomerEntity customerDB = customerRepository.findByNumberID(customer.getNumberID());
        if (customerDB != null) {
            return customerDB;
        }

        customer.setState("CREATED");
        customerDB = customerRepository.save(customer);
        return customerDB;
    }

    @Override
    public CustomerEntity updateCustomer(CustomerEntity customer) {
        CustomerEntity customerDB = getCustomer(customer.getId());
        if (customerDB == null) {
            return null;
        }
        customerDB.setFirstName(customer.getFirstName());
        customerDB.setLastName(customer.getLastName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhotoUrl(customer.getPhotoUrl());

        return customerRepository.save(customerDB);
    }

    @Override
    public CustomerEntity deleteCustomer(CustomerEntity customer) {
        CustomerEntity customerDB = getCustomer(customer.getId());
        if (customerDB == null) {
            return null;
        }
        customer.setState("DELETED");
        return customerRepository.save(customer);
    }

    @Override
    public CustomerEntity getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

}
