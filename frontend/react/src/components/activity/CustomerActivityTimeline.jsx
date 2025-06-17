import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './CustomerActivityTimeline.css';

const CustomerActivityTimeline = () => {
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [filter, setFilter] = useState('all');
    const [days, setDays] = useState(7);

    useEffect(() => {
        fetchActivities();
    }, [filter, days]);

    const fetchActivities = async () => {
        try {
            setLoading(true);
            let url = 'http://localhost:8088/api/v1/customers/activities';
            
            if (filter === 'recent') {
                url += `/recent?days=${days}`;
            }
            
            const response = await axios.get(url);
            setActivities(response.data);
        } catch (err) {
            setError('Failed to fetch activities');
            console.error('Error fetching activities:', err);
        } finally {
            setLoading(false);
        }
    };

    const getActivityIcon = (activityType) => {
        const icons = {
            'LOGIN': '🔐',
            'LOGOUT': '🚪',
            'PROFILE_UPDATE': '✏️',
            'PROFILE_IMAGE_UPLOAD': '📷',
            'PASSWORD_CHANGE': '🔑',
            'ACCOUNT_CREATED': '👤',
            'ACCOUNT_DELETED': '🗑️',
            'DATA_EXPORT': '📤',
            'DATA_IMPORT': '📥',
            'SEARCH_PERFORMED': '🔍',
            'ANALYTICS_VIEWED': '📊'
        };
        return icons[activityType] || '📝';
    };

    const getActivityColor = (activityType) => {
        const colors = {
            'LOGIN': '#28a745',
            'LOGOUT': '#6c757d',
            'PROFILE_UPDATE': '#007bff',
            'PROFILE_IMAGE_UPLOAD': '#17a2b8',
            'PASSWORD_CHANGE': '#ffc107',
            'ACCOUNT_CREATED': '#28a745',
            'ACCOUNT_DELETED': '#dc3545',
            'DATA_EXPORT': '#6f42c1',
            'DATA_IMPORT': '#fd7e14',
            'SEARCH_PERFORMED': '#20c997',
            'ANALYTICS_VIEWED': '#e83e8c'
        };
        return colors[activityType] || '#6c757d';
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleString();
    };

    const formatTimeAgo = (dateString) => {
        const date = new Date(dateString);
        const now = new Date();
        const diffInSeconds = Math.floor((now - date) / 1000);
        
        if (diffInSeconds < 60) return 'Just now';
        if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}m ago`;
        if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}h ago`;
        return `${Math.floor(diffInSeconds / 86400)}d ago`;
    };

    if (loading) {
        return (
            <div className="activity-container">
                <div className="loading">Loading activities...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="activity-container">
                <div className="error">{error}</div>
            </div>
        );
    }

    return (
        <div className="activity-container">
            <div className="activity-header">
                <h1>Customer Activity Timeline</h1>
                <div className="activity-controls">
                    <select 
                        value={filter} 
                        onChange={(e) => setFilter(e.target.value)}
                        className="filter-select"
                    >
                        <option value="all">All Activities</option>
                        <option value="recent">Recent Activities</option>
                    </select>
                    
                    {filter === 'recent' && (
                        <select 
                            value={days} 
                            onChange={(e) => setDays(parseInt(e.target.value))}
                            className="days-select"
                        >
                            <option value={1}>Last 24 hours</option>
                            <option value={7}>Last 7 days</option>
                            <option value={30}>Last 30 days</option>
                        </select>
                    )}
                    
                    <button onClick={fetchActivities} className="refresh-btn">
                        🔄 Refresh
                    </button>
                </div>
            </div>

            <div className="activity-stats">
                <div className="stat-card">
                    <span className="stat-icon">📊</span>
                    <div className="stat-content">
                        <h3>Total Activities</h3>
                        <p>{activities.length}</p>
                    </div>
                </div>
            </div>

            <div className="timeline-container">
                {activities.length === 0 ? (
                    <div className="no-activities">
                        <span className="no-activities-icon">📭</span>
                        <h3>No activities found</h3>
                        <p>There are no activities to display for the selected filter.</p>
                    </div>
                ) : (
                    <div className="timeline">
                        {activities.map((activity, index) => (
                            <div key={activity.id} className="timeline-item">
                                <div className="timeline-marker" style={{ backgroundColor: getActivityColor(activity.activityType) }}>
                                    {getActivityIcon(activity.activityType)}
                                </div>
                                
                                <div className="timeline-content">
                                    <div className="activity-header">
                                        <h3 className="activity-title">{activity.description}</h3>
                                        <span className="activity-time">{formatTimeAgo(activity.createdAt)}</span>
                                    </div>
                                    
                                    <div className="activity-details">
                                        <p className="activity-type">
                                            <strong>Type:</strong> {activity.activityType.replace('_', ' ')}
                                        </p>
                                        
                                        {activity.details && (
                                            <p className="activity-description">
                                                <strong>Details:</strong> {activity.details}
                                            </p>
                                        )}
                                        
                                        <p className="activity-customer">
                                            <strong>Customer ID:</strong> {activity.customerId}
                                        </p>
                                        
                                        <p className="activity-timestamp">
                                            <strong>Time:</strong> {formatDate(activity.createdAt)}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            <div className="activity-footer">
                <p>Showing {activities.length} activities</p>
                <p>Last updated: {new Date().toLocaleString()}</p>
            </div>
        </div>
    );
};

export default CustomerActivityTimeline; 