// src/components/steps/Step4PaymentStatus.tsx
import React, { useEffect, useState } from 'react';
import { useForm } from '../../context/FormContext';
import { useVerifyPurchase } from '../../hooks/useApi';

const Step4PaymentStatus: React.FC = () => {
  const { formData, resetForm } = useForm();
  const [verificationStatus, setVerificationStatus] = useState<'pending' | 'success' | 'failed'>('pending');
  const verifyMutation = useVerifyPurchase();

  // Auto-verify purchase every 10 seconds
  useEffect(() => {
    if (!formData.requestId) return;

    const verifyPurchase = async () => {
      try {
        await verifyMutation.mutateAsync(formData.requestId!);
        setVerificationStatus('success');
      } catch (error) {
        console.error('Verification failed:', error);
        // Continue polling - don't set to failed immediately
      }
    };

    // Initial verification
    verifyPurchase();

    // Set up polling
    const interval = setInterval(verifyPurchase, 10000);

    // Cleanup
    return () => clearInterval(interval);
  }, [formData.requestId, verifyMutation]);

  const handleNewPurchase = () => {
    resetForm();
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
    // You could show a toast notification here
  };

  return (
    <div className="max-w-md mx-auto">
      <div className="text-center mb-8">
        <div className="mx-auto flex items-center justify-center h-16 w-16 rounded-full bg-blue-100 mb-4">
          <svg className="h-8 w-8 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
          </svg>
        </div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2">
          Payment Instructions
        </h2>
        <p className="text-gray-600">
          Send the exact amount to complete your purchase
        </p>
      </div>

      {/* Payment Details */}
      <div className="bg-gray-50 rounded-lg p-6 mb-6">
        <div className="space-y-4">
          {/* Wallet Address */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Wallet Address
            </label>
            <div className="flex items-center space-x-2">
              <input
                type="text"
                value={formData.walletAddress || ''}
                readOnly
                className="flex-1 p-3 bg-white border border-gray-300 rounded-lg text-sm font-mono"
              />
              <button
                onClick={() => copyToClipboard(formData.walletAddress || '')}
                className="px-3 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                title="Copy address"
              >
                <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                </svg>
              </button>
            </div>
          </div>

          {/* QR Code */}
          {formData.qrCodeUrl && (
            <div className="text-center">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Or scan QR Code
              </label>
              <img
                src={formData.qrCodeUrl}
                alt="Payment QR Code"
                className="mx-auto border border-gray-300 rounded-lg"
              />
            </div>
          )}

          {/* Request ID */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Transaction ID
            </label>
            <div className="flex items-center space-x-2">
              <input
                type="text"
                value={formData.requestId || ''}
                readOnly
                className="flex-1 p-3 bg-white border border-gray-300 rounded-lg text-sm font-mono"
              />
              <button
                onClick={() => copyToClipboard(formData.requestId || '')}
                className="px-3 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                title="Copy transaction ID"
              >
                <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Status */}
      <div className="mb-6">
        {verificationStatus === 'pending' && (
          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <svg className="animate-spin h-5 w-5 text-yellow-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
              </div>
              <div className="ml-3">
                <p className="text-sm text-yellow-800">
                  Waiting for payment confirmation...
                </p>
              </div>
            </div>
          </div>
        )}

        {verificationStatus === 'success' && (
          <div className="bg-green-50 border border-green-200 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <svg className="h-5 w-5 text-green-600" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
              </div>
              <div className="ml-3">
                <p className="text-sm text-green-800 font-medium">
                  Payment confirmed! Data will be delivered shortly.
                </p>
              </div>
            </div>
          </div>
        )}

        {verificationStatus === 'failed' && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <svg className="h-5 w-5 text-red-600" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                </svg>
              </div>
              <div className="ml-3">
                <p className="text-sm text-red-800">
                  Payment verification failed. Please contact support.
                </p>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Actions */}
      <div className="space-y-3">
        <button
          onClick={handleNewPurchase}
          className="w-full bg-blue-600 text-white py-3 px-4 rounded-lg hover:bg-blue-700 transition-colors"
        >
          Make Another Purchase
        </button>
        
        <p className="text-center text-sm text-gray-500">
          Keep this page open until payment is confirmed
        </p>
      </div>
    </div>
  );
};

export default Step4PaymentStatus;
