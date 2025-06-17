import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './CustomerAnalytics.css';

const CustomerAnalytics = () => {
    const [analytics, setAnalytics] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchAnalytics();
    }, []);

    const fetchAnalytics = async () => {
        try {
            setLoading(true);
            const response = await axios.get('http://localhost:8088/api/v1/customers/analytics');
            setAnalytics(response.data);
        } catch (err) {
            setError('Failed to fetch analytics data');
            console.error('Error fetching analytics:', err);
        } finally {
            setLoading(false);
        }
    };

    const StatCard = ({ title, value, icon, color }) => (
        <div className="stat-card" style={{ borderLeftColor: color }}>
            <div className="stat-icon" style={{ backgroundColor: color }}>
                {icon}
            </div>
            <div className="stat-content">
                <h3 className="stat-title">{title}</h3>
                <p className="stat-value">{value}</p>
            </div>
        </div>
    );

    const ChartCard = ({ title, data, type = 'bar' }) => (
        <div className="chart-card">
            <h3 className="chart-title">{title}</h3>
            <div className="chart-container">
                {type === 'bar' && (
                    <div className="bar-chart">
                        {Object.entries(data).map(([key, value]) => (
                            <div key={key} className="bar-item">
                                <div className="bar-label">{key}</div>
                                <div className="bar-wrapper">
                                    <div 
                                        className="bar" 
                                        style={{ 
                                            width: `${(value / Math.max(...Object.values(data))) * 100}%`,
                                            backgroundColor: getRandomColor(key)
                                        }}
                                    ></div>
                                    <span className="bar-value">{value}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
                {type === 'pie' && (
                    <div className="pie-chart">
                        {Object.entries(data).map(([key, value], index) => (
                            <div key={key} className="pie-item">
                                <div 
                                    className="pie-slice" 
                                    style={{ 
                                        backgroundColor: getRandomColor(key),
                                        transform: `rotate(${index * 45}deg)`
                                    }}
                                ></div>
                                <span className="pie-label">{key}: {value}</span>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );

    const getRandomColor = (seed) => {
        const colors = [
            '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', 
            '#FFEAA7', '#DDA0DD', '#98D8C8', '#F7DC6F'
        ];
        const index = seed.charCodeAt(0) % colors.length;
        return colors[index];
    };

    if (loading) {
        return (
            <div className="analytics-container">
                <div className="loading">Loading analytics...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="analytics-container">
                <div className="error">{error}</div>
            </div>
        );
    }

    if (!analytics) {
        return (
            <div className="analytics-container">
                <div className="error">No analytics data available</div>
            </div>
        );
    }

    return (
        <div className="analytics-container">
            <div className="analytics-header">
                <h1>Customer Analytics Dashboard</h1>
                <button onClick={fetchAnalytics} className="refresh-btn">
                    🔄 Refresh
                </button>
            </div>

            <div className="stats-grid">
                <StatCard
                    title="Total Customers"
                    value={analytics.totalCustomers}
                    icon="👥"
                    color="#4ECDC4"
                />
                <StatCard
                    title="Average Age"
                    value={analytics.averageAge ? Math.round(analytics.averageAge) : 0}
                    icon="📊"
                    color="#45B7D1"
                />
                <StatCard
                    title="With Profile Images"
                    value={analytics.customersWithProfileImages}
                    icon="📷"
                    color="#96CEB4"
                />
                <StatCard
                    title="Recent Registrations"
                    value={analytics.recentRegistrations?.Recent || 0}
                    icon="🆕"
                    color="#FF6B6B"
                />
            </div>

            <div className="charts-grid">
                <ChartCard
                    title="Gender Distribution"
                    data={analytics.genderDistribution}
                    type="pie"
                />
                <ChartCard
                    title="Age Distribution"
                    data={analytics.ageDistribution}
                    type="bar"
                />
            </div>

            <div className="analytics-footer">
                <p>Last updated: {new Date().toLocaleString()}</p>
            </div>
        </div>
    );
};

export default CustomerAnalytics; 