import React, { useState, useEffect } from 'react';
import { Bell, Check, Trash2, AlertCircle, Info, Mail, X } from 'lucide-react';
import './CustomerNotificationCenter.css';

const CustomerNotificationCenter = () => {
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const [isOpen, setIsOpen] = useState(false);
    const [selectedCustomer, setSelectedCustomer] = useState(1); // Default to first customer
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [bulkNotification, setBulkNotification] = useState({
        title: '',
        message: '',
        priority: 'NORMAL',
        selectedCustomers: []
    });

    useEffect(() => {
        fetchCustomers();
        fetchNotifications();
        fetchUnreadCount();
    }, [selectedCustomer]);

    const fetchCustomers = async () => {
        try {
            const response = await fetch('/api/v1/customers');
            const data = await response.json();
            setCustomers(data);
        } catch (error) {
            console.error('Error fetching customers:', error);
        }
    };

    const fetchNotifications = async () => {
        try {
            const response = await fetch(`/api/v1/customers/${selectedCustomer}/notifications`);
            const data = await response.json();
            setNotifications(data);
        } catch (error) {
            console.error('Error fetching notifications:', error);
        }
    };

    const fetchUnreadCount = async () => {
        try {
            const response = await fetch(`/api/v1/customers/${selectedCustomer}/notifications/count`);
            const count = await response.json();
            setUnreadCount(count);
        } catch (error) {
            console.error('Error fetching unread count:', error);
        }
    };

    const markAsRead = async (notificationId) => {
        try {
            await fetch(`/api/v1/customers/notifications/${notificationId}/read`, {
                method: 'PUT'
            });
            fetchNotifications();
            fetchUnreadCount();
        } catch (error) {
            console.error('Error marking notification as read:', error);
        }
    };

    const markAllAsRead = async () => {
        try {
            await fetch(`/api/v1/customers/${selectedCustomer}/notifications/read-all`, {
                method: 'PUT'
            });
            fetchNotifications();
            fetchUnreadCount();
        } catch (error) {
            console.error('Error marking all notifications as read:', error);
        }
    };

    const deleteNotification = async (notificationId) => {
        try {
            await fetch(`/api/v1/customers/notifications/${notificationId}`, {
                method: 'DELETE'
            });
            fetchNotifications();
            fetchUnreadCount();
        } catch (error) {
            console.error('Error deleting notification:', error);
        }
    };

    const sendBulkNotification = async () => {
        if (!bulkNotification.title || !bulkNotification.message || bulkNotification.selectedCustomers.length === 0) {
            alert('Please fill in all fields and select at least one customer');
            return;
        }

        try {
            await fetch('/api/v1/customers/notifications/bulk', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    customerIds: bulkNotification.selectedCustomers,
                    title: bulkNotification.title,
                    message: bulkNotification.message,
                    priority: bulkNotification.priority
                })
            });
            
            setBulkNotification({
                title: '',
                message: '',
                priority: 'NORMAL',
                selectedCustomers: []
            });
            alert('Bulk notification sent successfully!');
        } catch (error) {
            console.error('Error sending bulk notification:', error);
            alert('Error sending bulk notification');
        }
    };

    const getPriorityIcon = (priority) => {
        switch (priority) {
            case 'URGENT':
                return <AlertCircle className="priority-icon urgent" />;
            case 'HIGH':
                return <AlertCircle className="priority-icon high" />;
            case 'NORMAL':
                return <Info className="priority-icon normal" />;
            case 'LOW':
                return <Info className="priority-icon low" />;
            default:
                return <Info className="priority-icon normal" />;
        }
    };

    const getPriorityColor = (priority) => {
        switch (priority) {
            case 'URGENT':
                return '#dc2626';
            case 'HIGH':
                return '#ea580c';
            case 'NORMAL':
                return '#2563eb';
            case 'LOW':
                return '#059669';
            default:
                return '#2563eb';
        }
    };

    const formatDate = (dateString) => {
        return new Date(dateString).toLocaleString();
    };

    return (
        <div className="notification-center">
            <div className="notification-header">
                <h2>🔔 Notification Center</h2>
                <div className="notification-controls">
                    <select 
                        value={selectedCustomer} 
                        onChange={(e) => setSelectedCustomer(parseInt(e.target.value))}
                        className="customer-select"
                    >
                        {customers.map(customer => (
                            <option key={customer.id} value={customer.id}>
                                {customer.name} ({customer.email})
                            </option>
                        ))}
                    </select>
                    <button 
                        className="notification-toggle"
                        onClick={() => setIsOpen(!isOpen)}
                    >
                        <Bell />
                        {unreadCount > 0 && <span className="notification-badge">{unreadCount}</span>}
                    </button>
                </div>
            </div>

            {isOpen && (
                <div className="notification-panel">
                    <div className="notification-actions">
                        <button onClick={markAllAsRead} className="action-btn">
                            <Check /> Mark All Read
                        </button>
                        <button onClick={() => setBulkNotification({...bulkNotification, selectedCustomers: []})} className="action-btn">
                            <Mail /> Send Bulk Notification
                        </button>
                    </div>

                    <div className="notifications-list">
                        {notifications.length === 0 ? (
                            <div className="no-notifications">
                                <Bell className="no-notifications-icon" />
                                <p>No notifications found</p>
                            </div>
                        ) : (
                            notifications.map(notification => (
                                <div 
                                    key={notification.id} 
                                    className={`notification-item ${!notification.isRead ? 'unread' : ''}`}
                                    style={{ borderLeftColor: getPriorityColor(notification.priority) }}
                                >
                                    <div className="notification-content">
                                        <div className="notification-header">
                                            {getPriorityIcon(notification.priority)}
                                            <h4>{notification.title}</h4>
                                            <span className="notification-type">{notification.notificationType}</span>
                                        </div>
                                        <p className="notification-message">{notification.message}</p>
                                        <div className="notification-meta">
                                            <span className="notification-date">
                                                {formatDate(notification.createdAt)}
                                            </span>
                                            <span className="notification-priority">
                                                {notification.priority}
                                            </span>
                                        </div>
                                    </div>
                                    <div className="notification-actions">
                                        {!notification.isRead && (
                                            <button 
                                                onClick={() => markAsRead(notification.id)}
                                                className="action-btn small"
                                                title="Mark as read"
                                            >
                                                <Check />
                                            </button>
                                        )}
                                        <button 
                                            onClick={() => deleteNotification(notification.id)}
                                            className="action-btn small delete"
                                            title="Delete notification"
                                        >
                                            <Trash2 />
                                        </button>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>

                    {/* Bulk Notification Form */}
                    <div className="bulk-notification-form">
                        <h3>Send Bulk Notification</h3>
                        <div className="form-group">
                            <label>Select Customers:</label>
                            <div className="customer-checkboxes">
                                {customers.map(customer => (
                                    <label key={customer.id} className="checkbox-label">
                                        <input
                                            type="checkbox"
                                            checked={bulkNotification.selectedCustomers.includes(customer.id)}
                                            onChange={(e) => {
                                                if (e.target.checked) {
                                                    setBulkNotification({
                                                        ...bulkNotification,
                                                        selectedCustomers: [...bulkNotification.selectedCustomers, customer.id]
                                                    });
                                                } else {
                                                    setBulkNotification({
                                                        ...bulkNotification,
                                                        selectedCustomers: bulkNotification.selectedCustomers.filter(id => id !== customer.id)
                                                    });
                                                }
                                            }}
                                        />
                                        {customer.name}
                                    </label>
                                ))}
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Title:</label>
                            <input
                                type="text"
                                value={bulkNotification.title}
                                onChange={(e) => setBulkNotification({...bulkNotification, title: e.target.value})}
                                placeholder="Notification title"
                            />
                        </div>
                        <div className="form-group">
                            <label>Message:</label>
                            <textarea
                                value={bulkNotification.message}
                                onChange={(e) => setBulkNotification({...bulkNotification, message: e.target.value})}
                                placeholder="Notification message"
                                rows="3"
                            />
                        </div>
                        <div className="form-group">
                            <label>Priority:</label>
                            <select
                                value={bulkNotification.priority}
                                onChange={(e) => setBulkNotification({...bulkNotification, priority: e.target.value})}
                            >
                                <option value="LOW">Low</option>
                                <option value="NORMAL">Normal</option>
                                <option value="HIGH">High</option>
                                <option value="URGENT">Urgent</option>
                            </select>
                        </div>
                        <button onClick={sendBulkNotification} className="send-btn">
                            Send Notification
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CustomerNotificationCenter; 