package com.matias.customermicroservice.service;

import com.matias.customermicroservice.entity.CustomerEntity;
import com.matias.customermicroservice.entity.RegionEntity;

import java.util.List;

public interface CustomerService {

    public List<CustomerEntity> findCustomerAll();

    public List<CustomerEntity> findCustomersByRegion(RegionEntity region);

    public CustomerEntity createCustomer(CustomerEntity customer);

    public CustomerEntity updateCustomer(CustomerEntity customer);

    public CustomerEntity deleteCustomer(CustomerEntity customer);

    public CustomerEntity getCustomer(Long id);

}
