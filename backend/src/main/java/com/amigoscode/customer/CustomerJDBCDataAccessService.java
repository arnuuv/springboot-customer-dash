package com.amigoscode.customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate,
                                         CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, password, age, gender, profile_image_id
                FROM customer
                LIMIT 1000
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT id, name, email, password, age, gender, profile_image_id
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, password, age, gender)
                VALUES (?, ?, ?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender().name()
        );

        System.out.println("insertCustomer result " + result);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, customerId);
        System.out.println("deleteCustomerById result = " + result);
    }

    @Override
    public void updateCustomer(Customer update) {
        if (update.getName() != null) {
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getName(),
                    update.getId()
            );
            System.out.println("update customer name result = " + result);
        }
        if (update.getAge() != null) {
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getAge(),
                    update.getId()
            );
            System.out.println("update customer age result = " + result);
        }
        if (update.getEmail() != null) {
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId());
            System.out.println("update customer email result = " + result);
        }
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        var sql = """
                SELECT id, name, email, password, age, gender, profile_image_id
                FROM customer
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, email)
                .stream()
                .findFirst();
    }

    @Override
    public void updateCustomerProfileImageId(String profileImageId,
                                             Integer customerId) {
        var sql = """
                UPDATE customer
                SET profile_image_id = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, profileImageId, customerId);
    }

    @Override
    public List<Customer> searchCustomers(String name, String email, Integer minAge, Integer maxAge, Gender gender) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT id, name, email, password, age, gender, profile_image_id FROM customer WHERE 1=1");
        
        java.util.List<Object> params = new java.util.ArrayList<>();
        
        if (name != null && !name.trim().isEmpty()) {
            sqlBuilder.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + name.trim() + "%");
        }
        
        if (email != null && !email.trim().isEmpty()) {
            sqlBuilder.append(" AND LOWER(email) LIKE LOWER(?)");
            params.add("%" + email.trim() + "%");
        }
        
        if (minAge != null) {
            sqlBuilder.append(" AND age >= ?");
            params.add(minAge);
        }
        
        if (maxAge != null) {
            sqlBuilder.append(" AND age <= ?");
            params.add(maxAge);
        }
        
        if (gender != null) {
            sqlBuilder.append(" AND gender = ?");
            params.add(gender.name());
        }
        
        sqlBuilder.append(" ORDER BY name LIMIT 1000");
        
        return jdbcTemplate.query(sqlBuilder.toString(), customerRowMapper, params.toArray());
    }

    @Override
    public CustomerAnalyticsDTO getCustomerAnalytics() {
        // Get total customers
        String totalCustomersSql = "SELECT COUNT(*) FROM customer";
        Long totalCustomers = jdbcTemplate.queryForObject(totalCustomersSql, Long.class);

        // Get gender distribution
        String genderDistributionSql = "SELECT gender, COUNT(*) FROM customer GROUP BY gender";
        Map<String, Long> genderDistribution = new java.util.HashMap<>();
        jdbcTemplate.query(genderDistributionSql, (rs, rowNum) -> {
            genderDistribution.put(rs.getString("gender"), rs.getLong("count"));
            return null;
        });

        // Get age distribution (grouped by decades)
        String ageDistributionSql = """
                SELECT 
                    CASE 
                        WHEN age < 20 THEN 'Under 20'
                        WHEN age BETWEEN 20 AND 29 THEN '20-29'
                        WHEN age BETWEEN 30 AND 39 THEN '30-39'
                        WHEN age BETWEEN 40 AND 49 THEN '40-49'
                        WHEN age >= 50 THEN '50+'
                    END as age_group,
                    COUNT(*) as count
                FROM customer 
                GROUP BY age_group
                ORDER BY age_group
                """;
        Map<String, Long> ageDistribution = new java.util.HashMap<>();
        jdbcTemplate.query(ageDistributionSql, (rs, rowNum) -> {
            ageDistribution.put(rs.getString("age_group"), rs.getLong("count"));
            return null;
        });

        // Get customers with profile images
        String profileImagesSql = "SELECT COUNT(*) FROM customer WHERE profile_image_id IS NOT NULL";
        Long customersWithProfileImages = jdbcTemplate.queryForObject(profileImagesSql, Long.class);

        // Get average age
        String averageAgeSql = "SELECT AVG(age) FROM customer";
        Double averageAge = jdbcTemplate.queryForObject(averageAgeSql, Double.class);

        // Get recent registrations (last 7 days - simplified to show recent customers)
        String recentRegistrationsSql = "SELECT COUNT(*) FROM customer WHERE id > (SELECT MAX(id) - 5 FROM customer)";
        Long recentRegistrations = jdbcTemplate.queryForObject(recentRegistrationsSql, Long.class);

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
