import React, { useState } from 'react';
import './UrlList.css';

const UrlList = ({ urls, loading }) => {
    const [copiedId, setCopiedId] = useState(null);

    const copyToClipboard = (url, id) => {
        navigator.clipboard.writeText(url);
        setCopiedId(id);
        setTimeout(() => setCopiedId(null), 2000);
    };

    if (loading) {
        return <div className="loading">Loading...</div>;
    }

    if (urls.length === 0) {
        return (
            <div className="empty-state">
                <h3>No URLs yet</h3>
                <p>Create your first short URL to get started!</p>
            </div>
        );
    }

    return (
        <div className="url-list-container">
            <h2>Your Short URLs</h2>
            <div className="url-table">
                {urls.map((url) => (
                    <div key={url.id} className="url-row">
                        <div className="url-info">
                            <div className="url-original">
                                <strong>Original:</strong> {url.originalUrl}
                            </div>
                            <div className="url-short">
                                <strong>Short URL:</strong>{' '}
                                <a href={url.shortUrl} target="_blank" rel="noopener noreferrer">
                                    {url.shortUrl}
                                </a>
                            </div>
                            <div className="url-meta">
                                <span className="url-clicks">{url.totalClicks} clicks</span>
                                <span className="url-date">
                                    Created: {new Date(url.createdAt).toLocaleDateString()}
                                </span>
                            </div>
                        </div>
                        <div className="url-actions">
                            <button
                                onClick={() => copyToClipboard(url.shortUrl, url.id)}
                                className="btn btn-secondary btn-sm"
                            >
                                {copiedId === url.id ? 'Copied!' : 'Copy'}
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default UrlList;
