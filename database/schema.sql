-- URL Shortener SaaS Database Schema
-- PostgreSQL Database

-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- URLs Table
CREATE TABLE urls (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    original_url TEXT NOT NULL,
    short_code VARCHAR(20) UNIQUE NOT NULL,
    total_clicks INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Click Analytics Table
CREATE TABLE click_analytics (
    id BIGSERIAL PRIMARY KEY,
    url_id BIGINT NOT NULL,
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT,
    referrer TEXT,
    FOREIGN KEY (url_id) REFERENCES urls(id) ON DELETE CASCADE
);

-- Payments Table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    payment_reference_id VARCHAR(255) UNIQUE NOT NULL,
    amount INT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_urls_user_id ON urls(user_id);
CREATE INDEX idx_urls_short_code ON urls(short_code);
CREATE INDEX idx_click_analytics_url_id ON click_analytics(url_id);
CREATE INDEX idx_click_analytics_clicked_at ON click_analytics(clicked_at);
CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_reference_id ON payments(payment_reference_id);
