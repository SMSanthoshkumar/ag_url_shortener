import React, { useState, useEffect } from 'react';
import { urlService } from '../../services/urlService';
import UrlList from './UrlList';
import AnalyticsChart from './AnalyticsChart';
import UrlShortener from '../url/UrlShortener';
import './Dashboard.css';

const Dashboard = () => {
    const [urls, setUrls] = useState([]);
    const [analytics, setAnalytics] = useState({});
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState('shorten');

    useEffect(() => {
        fetchUrls();
        fetchAnalytics();
    }, []);

    const fetchUrls = async () => {
        try {
            const data = await urlService.getUserUrls();
            setUrls(data);
            setLoading(false);
        } catch (err) {
            console.error('Error fetching URLs:', err);
            setLoading(false);
        }
    };

    const fetchAnalytics = async () => {
        try {
            const data = await urlService.getUserAnalytics();
            setAnalytics(data);
        } catch (err) {
            console.error('Error fetching analytics:', err);
        }
    };

    const handleUrlCreated = () => {
        fetchUrls();
        fetchAnalytics();
        setActiveTab('urls');
    };

    const totalClicks = urls.reduce((sum, url) => sum + url.totalClicks, 0);

    return (
        <div className="dashboard-container">
            <div className="dashboard-header">
                <h1>Dashboard</h1>
                <div className="stats-container">
                    <div className="stat-card">
                        <div className="stat-value">{urls.length}</div>
                        <div className="stat-label">Total URLs</div>
                    </div>
                    <div className="stat-card">
                        <div className="stat-value">{totalClicks}</div>
                        <div className="stat-label">Total Clicks</div>
                    </div>
                </div>
            </div>

            <div className="dashboard-tabs">
                <button
                    className={`tab ${activeTab === 'shorten' ? 'active' : ''}`}
                    onClick={() => setActiveTab('shorten')}
                >
                    Shorten URL
                </button>
                <button
                    className={`tab ${activeTab === 'urls' ? 'active' : ''}`}
                    onClick={() => setActiveTab('urls')}
                >
                    My URLs
                </button>
                <button
                    className={`tab ${activeTab === 'analytics' ? 'active' : ''}`}
                    onClick={() => setActiveTab('analytics')}
                >
                    Analytics
                </button>
            </div>

            <div className="dashboard-content">
                {activeTab === 'shorten' && <UrlShortener onUrlCreated={handleUrlCreated} />}
                {activeTab === 'urls' && <UrlList urls={urls} loading={loading} />}
                {activeTab === 'analytics' && <AnalyticsChart analytics={analytics} />}
            </div>
        </div>
    );
};

export default Dashboard;
