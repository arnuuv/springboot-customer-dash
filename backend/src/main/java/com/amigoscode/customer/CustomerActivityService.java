package com.amigoscode.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomerActivityService {

    private final CustomerActivityRepository activityRepository;

    public CustomerActivityService(CustomerActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void logActivity(Integer customerId, ActivityType activityType, String description, String details) {
        CustomerActivity activity = new CustomerActivity(customerId, activityType, description, details);
        activityRepository.save(activity);
    }

    public void logActivity(Integer customerId, ActivityType activityType, String description) {
        logActivity(customerId, activityType, description, null);
    }

    public List<CustomerActivity> getCustomerActivities(Integer customerId) {
        return activityRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public List<CustomerActivity> getCustomerActivitiesByType(Integer customerId, ActivityType activityType) {
        return activityRepository.findByCustomerIdAndActivityTypeOrderByCreatedAtDesc(customerId, activityType);
    }

    public List<CustomerActivity> getRecentActivities(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return activityRepository.findRecentActivities(startDate);
    }

    public List<CustomerActivity> getCustomerRecentActivities(Integer customerId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return activityRepository.findCustomerRecentActivities(customerId, startDate);
    }

    public List<CustomerActivity> getAllActivities() {
        return activityRepository.findAll();
    }
} 