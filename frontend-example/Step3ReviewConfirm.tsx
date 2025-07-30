// src/components/steps/Step3ReviewConfirm.tsx
import React from 'react';
import { useForm } from '../../context/FormContext';
import { useBuyPrice, usePurchaseData } from '../../hooks/useApi';

const Step3ReviewConfirm: React.FC = () => {
  const { formData, updateFormData, nextStep, prevStep } = useForm();
  const cryptoPair = `${formData.cryptoCurrency}ngn`;
  
  const { data: priceData, isLoading: priceLoading } = useBuyPrice(cryptoPair);
  const purchaseMutation = usePurchaseData();

  const handleConfirmPurchase = async () => {
    try {
      const purchaseData = {
        phoneNumber: formData.phoneNumber,
        network: formData.network,
        cryptoCurrency: formData.cryptoCurrency,
        dataPlanCode: formData.dataPlan,
      };

      const result = await purchaseMutation.mutateAsync(purchaseData);
      
      // Update form data with purchase result
      updateFormData({
        requestId: result.requestId,
        walletAddress: result.walletAddress,
        qrCodeUrl: result.qrCodeUrl,
      });
      
      nextStep();
    } catch (error) {
      console.error('Purchase failed:', error);
      // Handle error (show toast, etc.)
    }
  };

  // Mock data plan details (in real app, you'd fetch this)
  const dataPlanDetails = {
    name: 'MTN 1GB Data',
    amount: 350, // This would come from the selected plan
  };

  const cryptoAmount = priceData?.bid ? dataPlanDetails.amount / priceData.bid : 0;

  return (
    <div className="max-w-md mx-auto">
      <h2 className="text-2xl font-bold mb-6 text-center">
        Review & Confirm Purchase
      </h2>

      <div className="bg-gray-50 rounded-lg p-6 space-y-4 mb-6">
        {/* Purchase Summary */}
        <div className="border-b pb-4">
          <h3 className="font-semibold text-lg mb-3">Purchase Summary</h3>
          
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span className="text-gray-600">Phone Number:</span>
              <span className="font-medium">{formData.phoneNumber}</span>
            </div>
            
            <div className="flex justify-between">
              <span className="text-gray-600">Network:</span>
              <span className="font-medium capitalize">{formData.network}</span>
            </div>
            
            <div className="flex justify-between">
              <span className="text-gray-600">Data Plan:</span>
              <span className="font-medium">{dataPlanDetails.name}</span>
            </div>
          </div>
        </div>

        {/* Payment Details */}
        <div>
          <h3 className="font-semibold text-lg mb-3">Payment Details</h3>
          
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span className="text-gray-600">Amount (NGN):</span>
              <span className="font-medium">₦{dataPlanDetails.amount.toLocaleString()}</span>
            </div>
            
            <div className="flex justify-between">
              <span className="text-gray-600">Pay with:</span>
              <span className="font-medium uppercase">{formData.cryptoCurrency}</span>
            </div>
            
            {priceLoading ? (
              <div className="flex justify-between">
                <span className="text-gray-600">Crypto Amount:</span>
                <span className="text-gray-400">Loading...</span>
              </div>
            ) : (
              <div className="flex justify-between">
                <span className="text-gray-600">Crypto Amount:</span>
                <span className="font-medium">
                  {cryptoAmount.toFixed(8)} {formData.cryptoCurrency.toUpperCase()}
                </span>
              </div>
            )}
            
            {priceData && (
              <div className="flex justify-between text-xs text-gray-500">
                <span>Exchange Rate:</span>
                <span>1 {formData.cryptoCurrency.toUpperCase()} = ₦{priceData.bid.toLocaleString()}</span>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Important Notice */}
      <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
        <div className="flex">
          <div className="flex-shrink-0">
            <svg className="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
              <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
            </svg>
          </div>
          <div className="ml-3">
            <p className="text-sm text-yellow-800">
              Please review all details carefully. Once confirmed, you'll receive payment instructions.
            </p>
          </div>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="flex space-x-4">
        <button
          type="button"
          onClick={prevStep}
          disabled={purchaseMutation.isPending}
          className="flex-1 bg-gray-200 text-gray-700 py-3 px-4 rounded-lg hover:bg-gray-300 disabled:opacity-50 transition-colors"
        >
          Back
        </button>
        <button
          onClick={handleConfirmPurchase}
          disabled={purchaseMutation.isPending || priceLoading}
          className="flex-1 bg-green-600 text-white py-3 px-4 rounded-lg hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors flex items-center justify-center"
        >
          {purchaseMutation.isPending ? (
            <>
              <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Processing...
            </>
          ) : (
            'Confirm Purchase'
          )}
        </button>
      </div>

      {/* Error Display */}
      {purchaseMutation.error && (
        <div className="mt-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          <p className="text-sm">
            Purchase failed. Please try again or contact support.
          </p>
        </div>
      )}
    </div>
  );
};

export default Step3ReviewConfirm;
