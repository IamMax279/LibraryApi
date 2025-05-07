package services;

import dto.CustomerDto;
import models.Customer;

public interface CustomerService {
    void createCustomer(CustomerDto customerDto) throws Exception;
    String customerExists(CustomerDto customerDto);
    Customer findByEmail(String email) throws Exception;
    void addToRented(String title, String email) throws Exception;
    void bringBookBack(String title, String email) throws Exception;
}