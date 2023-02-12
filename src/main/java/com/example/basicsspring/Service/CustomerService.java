package com.example.basicsspring.Service;

import com.example.basicsspring.Model.Customer;

import java.util.Collection;

public interface CustomerService {

    Collection<Customer> save(String...names);

    Customer findById(Long id);

    Collection<Customer> findAll();
}
