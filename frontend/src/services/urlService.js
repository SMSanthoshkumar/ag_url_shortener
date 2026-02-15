import api from './api';

export const urlService = {
    createShortUrl: async (originalUrl, paymentReferenceId) => {
        const response = await api.post('/url/shorten', {
            originalUrl,
            paymentReferenceId
        });
        return response.data;
    },

    getUserUrls: async () => {
        const response = await api.get('/url/user');
        return response.data;
    },

    getUserAnalytics: async () => {
        const response = await api.get('/analytics/user');
        return response.data;
    },

    getUrlAnalytics: async (shortCode) => {
        const response = await api.get(`/analytics/${shortCode}`);
        return response.data;
    }
};
