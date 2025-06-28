package com.amigoscode.customer;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amigoscode.jwt.JWTUtil;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;
    private final CustomerExportImportService exportImportService;
    private final CustomerActivityService activityService;
    private final CustomerNotificationService notificationService;

    public CustomerController(CustomerService customerService,
                              JWTUtil jwtUtil,
                              CustomerExportImportService exportImportService,
                              CustomerActivityService activityService,
                              CustomerNotificationService notificationService) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
        this.exportImportService = exportImportService;
        this.activityService = activityService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public CustomerDTO getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(
            @PathVariable("customerId") Integer customerId) {
        customerService.deleteCustomerById(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CustomerUpdateRequest updateRequest) {
        customerService.updateCustomer(customerId, updateRequest);
    }

    @PostMapping(
            value = "{customerId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCustomerProfileImage(
            @PathVariable("customerId") Integer customerId,
            @RequestParam("file") MultipartFile file) {
        customerService.uploadCustomerProfileImage(customerId, file);
    }

    @GetMapping(
            value = "{customerId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCustomerProfileImage(
            @PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerProfileImage(customerId);
    }

    @PostMapping("search")
    public List<CustomerDTO> searchCustomers(
            @RequestBody CustomerSearchRequest searchRequest) {
        return customerService.searchCustomers(searchRequest);
    }

    @GetMapping("analytics")
    public CustomerAnalyticsDTO getCustomerAnalytics() {
        return customerService.getCustomerAnalytics();
    }

    @GetMapping("export/csv")
    public ResponseEntity<byte[]> exportCustomersToCSV() {
        byte[] csvData = exportImportService.exportCustomersToCSV();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customers.csv\"")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvData);
    }

    @PostMapping("import/csv")
    public CustomerExportImportService.ImportResult importCustomersFromCSV(
            @RequestParam("file") MultipartFile file) {
        return exportImportService.importCustomersFromCSV(file);
    }

    @GetMapping("activities")
    public List<CustomerActivity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @GetMapping("{customerId}/activities")
    public List<CustomerActivity> getCustomerActivities(
            @PathVariable("customerId") Integer customerId) {
        return activityService.getCustomerActivities(customerId);
    }

    @GetMapping("activities/recent")
    public List<CustomerActivity> getRecentActivities(
            @RequestParam(defaultValue = "7") int days) {
        return activityService.getRecentActivities(days);
    }

    // Notification endpoints
    @GetMapping("{customerId}/notifications")
    public List<CustomerNotification> getCustomerNotifications(
            @PathVariable("customerId") Integer customerId) {
        return notificationService.getCustomerNotifications(customerId);
    }

    @GetMapping("{customerId}/notifications/unread")
    public List<CustomerNotification> getUnreadNotifications(
            @PathVariable("customerId") Integer customerId) {
        return notificationService.getUnreadNotifications(customerId);
    }

    @GetMapping("{customerId}/notifications/count")
    public Long getUnreadNotificationCount(
            @PathVariable("customerId") Integer customerId) {
        return notificationService.getUnreadCount(customerId);
    }

    @PutMapping("notifications/{notificationId}/read")
    public void markNotificationAsRead(@PathVariable("notificationId") Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    @PutMapping("{customerId}/notifications/read-all")
    public void markAllNotificationsAsRead(@PathVariable("customerId") Integer customerId) {
        notificationService.markAllAsRead(customerId);
    }

    @DeleteMapping("notifications/{notificationId}")
    public void deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @PostMapping("notifications/bulk")
    public void sendBulkNotification(@RequestBody BulkNotificationRequest request) {
        notificationService.sendBulkNotification(request.customerIds(), request.title(), request.message(), request.priority());
    }

    @PostMapping("notifications/system")
    public void sendSystemNotification(@RequestBody SystemNotificationRequest request) {
        notificationService.sendSystemNotification(request.title(), request.message(), request.priority());
    }

}
