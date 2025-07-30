// src/components/steps/Step1NetworkSelection.tsx
import React from 'react';
import { useForm } from '../../context/FormContext';
import { useNetworks, useCryptoCurrencies } from '../../hooks/useApi';

const Step1NetworkSelection: React.FC = () => {
  const { formData, updateFormData, nextStep } = useForm();
  const { data: networks, isLoading: networksLoading, error: networksError } = useNetworks();
  const { data: cryptoCurrencies, isLoading: cryptoLoading, error: cryptoError } = useCryptoCurrencies();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.network && formData.cryptoCurrency) {
      nextStep();
    }
  };

  if (networksLoading || cryptoLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (networksError || cryptoError) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        Error loading options. Please try again.
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto">
      <h2 className="text-2xl font-bold mb-6 text-center">
        Select Network & Cryptocurrency
      </h2>
      
      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Network Selection */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Select Network
          </label>
          <div className="grid grid-cols-2 gap-3">
            {networks?.map((network) => (
              <button
                key={network}
                type="button"
                onClick={() => updateFormData({ network })}
                className={`p-4 border-2 rounded-lg text-center capitalize transition-colors ${
                  formData.network === network
                    ? 'border-blue-500 bg-blue-50 text-blue-700'
                    : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                {network}
              </button>
            ))}
          </div>
        </div>

        {/* Crypto Currency Selection */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Select Cryptocurrency
          </label>
          <div className="grid grid-cols-3 gap-3">
            {cryptoCurrencies?.map((crypto) => (
              <button
                key={crypto}
                type="button"
                onClick={() => updateFormData({ cryptoCurrency: crypto })}
                className={`p-4 border-2 rounded-lg text-center uppercase transition-colors ${
                  formData.cryptoCurrency === crypto
                    ? 'border-blue-500 bg-blue-50 text-blue-700'
                    : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                {crypto}
              </button>
            ))}
          </div>
        </div>

        {/* Continue Button */}
        <button
          type="submit"
          disabled={!formData.network || !formData.cryptoCurrency}
          className="w-full bg-blue-600 text-white py-3 px-4 rounded-lg hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
        >
          Continue
        </button>
      </form>
    </div>
  );
};

export default Step1NetworkSelection;
