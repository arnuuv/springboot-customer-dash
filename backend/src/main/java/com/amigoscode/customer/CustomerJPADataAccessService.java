package com.amigoscode.customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        Page<Customer> page = customerRepository.findAll(Pageable.ofSize(1000));
        return page.getContent();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return customerRepository.existsCustomerById(id);
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(Customer update) {
        customerRepository.save(update);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    @Override
    public void updateCustomerProfileImageId(String profileImageId,
                                             Integer customerId) {
        customerRepository.updateProfileImageId(profileImageId, customerId);
    }

    @Override
    public List<Customer> searchCustomers(String name, String email, Integer minAge, Integer maxAge, Gender gender) {
        return customerRepository.findCustomersByFilters(name, email, minAge, maxAge, gender);
    }

    @Override
    public CustomerAnalyticsDTO getCustomerAnalytics() {
        Long totalCustomers = customerRepository.countTotalCustomers();
        
        Map<String, Long> genderDistribution = new java.util.HashMap<>();
        customerRepository.getGenderDistribution().forEach(result -> {
            genderDistribution.put(((Gender) result[0]).name(), (Long) result[1]);
        });

        // For age distribution, we'll use a simplified approach with JPA
        Map<String, Long> ageDistribution = new java.util.HashMap<>();
        List<Customer> allCustomers = customerRepository.findAll();
        
        long under20 = allCustomers.stream().filter(c -> c.getAge() < 20).count();
        long age20to29 = allCustomers.stream().filter(c -> c.getAge() >= 20 && c.getAge() <= 29).count();
        long age30to39 = allCustomers.stream().filter(c -> c.getAge() >= 30 && c.getAge() <= 39).count();
        long age40to49 = allCustomers.stream().filter(c -> c.getAge() >= 40 && c.getAge() <= 49).count();
        long age50plus = allCustomers.stream().filter(c -> c.getAge() >= 50).count();
        
        ageDistribution.put("Under 20", under20);
        ageDistribution.put("20-29", age20to29);
        ageDistribution.put("30-39", age30to39);
        ageDistribution.put("40-49", age40to49);
        ageDistribution.put("50+", age50plus);

        Long customersWithProfileImages = customerRepository.countCustomersWithProfileImages();
        Double averageAge = customerRepository.getAverageAge();
        Long recentRegistrations = customerRepository.getRecentRegistrations();

        return new CustomerAnalyticsDTO(
                totalCustomers,
                genderDistribution,
                ageDistribution,
                customersWithProfileImages,
                averageAge,
                Map.of("Recent", recentRegistrations)
        );
    }

}
