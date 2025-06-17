package com.amigoscode.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CustomerExportImportService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;

    public CustomerExportImportService(CustomerDao customerDao, PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
    }

    public byte[] exportCustomersToCSV() {
        List<Customer> customers = customerDao.selectAllCustomers();
        
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Name,Email,Age,Gender,Profile Image ID\n");
        
        for (Customer customer : customers) {
            csv.append(String.format("%d,%s,%s,%d,%s,%s\n",
                    customer.getId(),
                    escapeCsvField(customer.getName()),
                    escapeCsvField(customer.getEmail()),
                    customer.getAge(),
                    customer.getGender().name(),
                    customer.getProfileImageId() != null ? escapeCsvField(customer.getProfileImageId()) : ""
            ));
        }
        
        return csv.toString().getBytes();
    }

    public ImportResult importCustomersFromCSV(MultipartFile file) {
        ImportResult result = new ImportResult(0, 0, new ArrayList<>());
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                try {
                    Customer customer = parseCustomerFromCSV(line);
                    if (customer != null) {
                        if (!customerDao.existsCustomerWithEmail(customer.getEmail())) {
                            customerDao.insertCustomer(customer);
                            result.successCount++;
                        } else {
                            result.errors.add("Email already exists: " + customer.getEmail());
                            result.errorCount++;
                        }
                    }
                } catch (Exception e) {
                    result.errors.add("Error parsing line: " + line + " - " + e.getMessage());
                    result.errorCount++;
                }
            }
        } catch (IOException e) {
            result.errors.add("Error reading file: " + e.getMessage());
            result.errorCount++;
        }
        
        return result;
    }

    private Customer parseCustomerFromCSV(String line) {
        String[] fields = parseCsvLine(line);
        if (fields.length < 5) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        
        try {
            String name = unescapeCsvField(fields[1]);
            String email = unescapeCsvField(fields[2]);
            int age = Integer.parseInt(fields[3]);
            Gender gender = Gender.valueOf(fields[4]);
            
            // Generate a random password for imported customers
            String password = UUID.randomUUID().toString().substring(0, 8);
            
            return new Customer(name, email, passwordEncoder.encode(password), age, gender);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing customer data: " + e.getMessage());
        }
    }

    private String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    private String unescapeCsvField(String field) {
        if (field == null) return "";
        if (field.startsWith("\"") && field.endsWith("\"")) {
            return field.substring(1, field.length() - 1).replace("\"\"", "\"");
        }
        return field;
    }

    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++; // Skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }

    public static class ImportResult {
        public int successCount;
        public int errorCount;
        public List<String> errors;

        public ImportResult(int successCount, int errorCount, List<String> errors) {
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.errors = errors;
        }
    }
} 