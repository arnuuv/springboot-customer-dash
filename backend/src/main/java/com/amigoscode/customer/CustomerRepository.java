package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

@Transactional
public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {

    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
    Optional<Customer> findCustomerByEmail(String email);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Customer c SET c.profileImageId = ?1 WHERE c.id = ?2")
    int updateProfileImageId(String profileImageId, Integer customerId);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:minAge IS NULL OR c.age >= :minAge) AND " +
           "(:maxAge IS NULL OR c.age <= :maxAge) AND " +
           "(:gender IS NULL OR c.gender = :gender) " +
           "ORDER BY c.name")
    List<Customer> findCustomersByFilters(
            @Param("name") String name,
            @Param("email") String email,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("gender") Gender gender
    );
}
