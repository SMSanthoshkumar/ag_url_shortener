import React, { useState } from 'react';
import { paymentService } from '../../services/paymentService';
import { urlService } from '../../services/urlService';
import './UrlShortener.css';

const UrlShortener = ({ onUrlCreated }) => {
    const [originalUrl, setOriginalUrl] = useState('');
    const [qrCodeData, setQrCodeData] = useState(null);
    const [shortUrl, setShortUrl] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [step, setStep] = useState('input'); // input, payment, success

    const handleGenerateQr = async (e) => {
        e.preventDefault();
        if (!originalUrl) {
            setError('Please enter a URL');
            return;
        }

        setLoading(true);
        setError('');

        try {
            const qrData = await paymentService.generateQrCode();
            setQrCodeData(qrData);
            setStep('payment');
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to generate QR code');
        } finally {
            setLoading(false);
        }
    };

    const handleConfirmPayment = async () => {
        setLoading(true);
        setError('');

        try {
            // Confirm payment
            await paymentService.confirmPayment(qrCodeData.paymentReferenceId);

            // Create short URL
            const response = await urlService.createShortUrl(originalUrl, qrCodeData.paymentReferenceId);
            setShortUrl(response.shortUrl);
            setStep('success');

            if (onUrlCreated) {
                onUrlCreated();
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to create short URL');
        } finally {
            setLoading(false);
        }
    };

    const handleReset = () => {
        setOriginalUrl('');
        setQrCodeData(null);
        setShortUrl('');
        setStep('input');
        setError('');
    };

    const copyToClipboard = () => {
        navigator.clipboard.writeText(shortUrl);
        alert('Short URL copied to clipboard!');
    };

    return (
        <div className="url-shortener-container">
            {step === 'input' && (
                <div className="url-form">
                    <h2>Shorten Your URL</h2>
                    <form onSubmit={handleGenerateQr}>
                        <div className="form-group">
                            <label htmlFor="originalUrl">Enter your long URL</label>
                            <input
                                type="url"
                                id="originalUrl"
                                value={originalUrl}
                                onChange={(e) => setOriginalUrl(e.target.value)}
                                placeholder="https://example.com/very-long-url"
                                required
                            />
                        </div>
                        {error && <div className="error-message">{error}</div>}
                        <button type="submit" className="btn btn-primary" disabled={loading}>
                            {loading ? 'Loading...' : 'Generate Payment QR Code'}
                        </button>
                    </form>
                </div>
            )}

            {step === 'payment' && qrCodeData && (
                <div className="payment-section">
                    <h2>Complete Payment</h2>
                    <div className="qr-code-container">
                        <img
                            src={`data:image/png;base64,${qrCodeData.qrCodeBase64}`}
                            alt="Payment QR Code"
                            className="qr-code-image"
                        />
                    </div>
                    <div className="payment-details">
                        <h3>Payment Details</h3>
                        <p><strong>Amount:</strong> ₹{(qrCodeData.amount / 100).toFixed(2)}</p>
                        <p><strong>UPI ID:</strong> {qrCodeData.upiId}</p>
                        <p><strong>Merchant:</strong> {qrCodeData.merchantName}</p>
                        <p><strong>Reference:</strong> {qrCodeData.paymentReferenceId}</p>
                    </div>
                    <div className="payment-instructions">
                        <h4>How to Pay:</h4>
                        <ol>
                            <li>Open any UPI app (Google Pay, PhonePe, Paytm, etc.)</li>
                            <li>Scan the QR code above</li>
                            <li>Complete the payment</li>
                            <li>Click "I've Completed Payment" below</li>
                        </ol>
                    </div>
                    {error && <div className="error-message">{error}</div>}
                    <div className="payment-actions">
                        <button
                            onClick={handleConfirmPayment}
                            className="btn btn-primary"
                            disabled={loading}
                        >
                            {loading ? 'Creating Short URL...' : "I've Completed Payment"}
                        </button>
                        <button onClick={handleReset} className="btn btn-outline">
                            Cancel
                        </button>
                    </div>
                </div>
            )}

            {step === 'success' && shortUrl && (
                <div className="success-section">
                    <h2>✅ URL Shortened Successfully!</h2>
                    <div className="short-url-display">
                        <input type="text" value={shortUrl} readOnly />
                        <button onClick={copyToClipboard} className="btn btn-secondary">
                            Copy
                        </button>
                    </div>
                    <button onClick={handleReset} className="btn btn-primary">
                        Shorten Another URL
                    </button>
                </div>
            )}
        </div>
    );
};

export default UrlShortener;
