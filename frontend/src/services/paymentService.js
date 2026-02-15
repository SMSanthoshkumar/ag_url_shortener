import api from './api';

export const paymentService = {
    generateQrCode: async () => {
        const response = await api.post('/payment/generate-qr');
        return response.data;
    },

    confirmPayment: async (paymentReferenceId) => {
        const response = await api.post('/payment/confirm', null, {
            params: { paymentReferenceId }
        });
        return response.data;
    }
};
