package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerId);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerById(Integer customerId);
    void deleteCustomerById(Integer customerId);
    void updateCustomer(Customer update);
    Optional<Customer> selectUserByEmail(String email);
    List<Customer> searchCustomers(String name, String email, Integer minAge, Integer maxAge, Gender gender);
    void updateCustomerProfileImageId(String profileImageId, Integer customerId);
    CustomerAnalyticsDTO getCustomerAnalytics();
}
