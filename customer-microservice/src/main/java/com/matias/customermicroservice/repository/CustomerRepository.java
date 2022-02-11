package com.matias.customermicroservice.repository;

import com.matias.customermicroservice.entity.CustomerEntity;
import com.matias.customermicroservice.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    public CustomerEntity findByNumberID(String numberID);

    public List<CustomerEntity> findByLastName(String lastName);

    public List<CustomerEntity> findByRegion(RegionEntity region);

}
