// src/components/steps/Step2DataPlanSelection.tsx
import React, { useState } from 'react';
import { useForm } from '../../context/FormContext';
import { useDataPlans, useBuyPrice } from '../../hooks/useApi';

const Step2DataPlanSelection: React.FC = () => {
  const { formData, updateFormData, nextStep, prevStep } = useForm();
  const [phoneNumber, setPhoneNumber] = useState(formData.phoneNumber);
  const [selectedPlan, setSelectedPlan] = useState(formData.dataPlan);

  // Fetch data plans for selected network
  const { data: dataPlans, isLoading: plansLoading, error: plansError } = useDataPlans(formData.network);
  
  // Get crypto price for selected pair
  const cryptoPair = `${formData.cryptoCurrency}ngn`;
  const { data: priceData } = useBuyPrice(cryptoPair);

  const calculateCryptoAmount = (nairaAmount: number) => {
    if (!priceData?.bid) return 0;
    return nairaAmount / priceData.bid;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (phoneNumber && selectedPlan) {
      updateFormData({ 
        phoneNumber, 
        dataPlan: selectedPlan 
      });
      nextStep();
    }
  };

  const validatePhoneNumber = (phone: string) => {
    const phoneRegex = /^(\+234|0)[789][01]\d{8}$/;
    return phoneRegex.test(phone);
  };

  if (plansLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (plansError) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        Error loading data plans. Please go back and try again.
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto">
      <h2 className="text-2xl font-bold mb-6 text-center">
        Select Data Plan & Enter Phone Number
      </h2>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Phone Number Input */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Phone Number
          </label>
          <input
            type="tel"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            placeholder="08012345678"
            className={`w-full p-3 border-2 rounded-lg focus:outline-none focus:border-blue-500 ${
              phoneNumber && !validatePhoneNumber(phoneNumber)
                ? 'border-red-300'
                : 'border-gray-300'
            }`}
          />
          {phoneNumber && !validatePhoneNumber(phoneNumber) && (
            <p className="text-red-500 text-sm mt-1">
              Please enter a valid Nigerian phone number
            </p>
          )}
        </div>

        {/* Data Plans Selection */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Select Data Plan
          </label>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {dataPlans?.map((plan) => {
              const nairaAmount = parseFloat(plan.variation_amount);
              const cryptoAmount = calculateCryptoAmount(nairaAmount);
              
              return (
                <div
                  key={plan.variation_code}
                  onClick={() => setSelectedPlan(plan.variation_code)}
                  className={`p-4 border-2 rounded-lg cursor-pointer transition-colors ${
                    selectedPlan === plan.variation_code
                      ? 'border-blue-500 bg-blue-50'
                      : 'border-gray-200 hover:border-gray-300'
                  }`}
                >
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-semibold">{plan.name}</h3>
                      <p className="text-gray-600 text-sm">
                        ₦{nairaAmount.toLocaleString()}
                      </p>
                      {priceData && (
                        <p className="text-blue-600 text-sm">
                          ≈ {cryptoAmount.toFixed(8)} {formData.cryptoCurrency.toUpperCase()}
                        </p>
                      )}
                    </div>
                    <div className={`w-4 h-4 rounded-full border-2 ${
                      selectedPlan === plan.variation_code
                        ? 'border-blue-500 bg-blue-500'
                        : 'border-gray-300'
                    }`}></div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex space-x-4">
          <button
            type="button"
            onClick={prevStep}
            className="flex-1 bg-gray-200 text-gray-700 py-3 px-4 rounded-lg hover:bg-gray-300 transition-colors"
          >
            Back
          </button>
          <button
            type="submit"
            disabled={!phoneNumber || !selectedPlan || !validatePhoneNumber(phoneNumber)}
            className="flex-1 bg-blue-600 text-white py-3 px-4 rounded-lg hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
          >
            Continue
          </button>
        </div>
      </form>
    </div>
  );
};

export default Step2DataPlanSelection;
