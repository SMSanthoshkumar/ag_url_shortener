import React from 'react';
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
    ResponsiveContainer,
} from 'recharts';
import './AnalyticsChart.css';

const AnalyticsChart = ({ analytics }) => {
    // Convert analytics object to array for Recharts
    const chartData = Object.entries(analytics || {}).map(([date, clicks]) => ({
        date: new Date(date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
        clicks: clicks,
    }));

    if (chartData.length === 0) {
        return (
            <div className="analytics-container">
                <h2>Analytics</h2>
                <div className="empty-state">
                    <p>No analytics data yet. Start creating URLs and get clicks to see analytics!</p>
                </div>
            </div>
        );
    }

    const totalClicks = chartData.reduce((sum, data) => sum + data.clicks, 0);

    return (
        <div className="analytics-container">
            <div className="analytics-header">
                <h2>Click Analytics</h2>
                <div className="analytics-summary">
                    <div className="summary-item">
                        <span className="summary-label">Total Clicks</span>
                        <span className="summary-value">{totalClicks}</span>
                    </div>
                    <div className="summary-item">
                        <span className="summary-label">Days Tracked</span>
                        <span className="summary-value">{chartData.length}</span>
                    </div>
                </div>
            </div>

            <div className="chart-container">
                <ResponsiveContainer width="100%" height={400}>
                    <LineChart data={chartData}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                        <XAxis
                            dataKey="date"
                            stroke="#718096"
                            style={{ fontSize: '12px' }}
                        />
                        <YAxis
                            stroke="#718096"
                            style={{ fontSize: '12px' }}
                        />
                        <Tooltip
                            contentStyle={{
                                background: 'white',
                                border: '2px solid #e2e8f0',
                                borderRadius: '8px',
                            }}
                        />
                        <Legend />
                        <Line
                            type="monotone"
                            dataKey="clicks"
                            stroke="#667eea"
                            strokeWidth={3}
                            dot={{ fill: '#667eea', r: 4 }}
                            activeDot={{ r: 6 }}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default AnalyticsChart;
