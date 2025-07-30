// src/App.tsx
import React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { FormProvider, useForm } from './context/FormContext';
import Step1NetworkSelection from './components/steps/Step1NetworkSelection';
import Step2DataPlanSelection from './components/steps/Step2DataPlanSelection';
import Step3ReviewConfirm from './components/steps/Step3ReviewConfirm';
import Step4PaymentStatus from './components/steps/Step4PaymentStatus';

// Create a client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60 * 1000, // 1 minute
      retry: 2,
    },
  },
});

// Progress indicator component
const ProgressIndicator: React.FC<{ currentStep: number }> = ({ currentStep }) => {
  const steps = [
    { number: 1, title: 'Network & Crypto' },
    { number: 2, title: 'Data Plan' },
    { number: 3, title: 'Review' },
    { number: 4, title: 'Payment' },
  ];

  return (
    <div className="mb-8">
      <div className="flex items-center justify-center">
        {steps.map((step, index) => (
          <React.Fragment key={step.number}>
            <div className="flex flex-col items-center">
              <div
                className={`w-10 h-10 rounded-full flex items-center justify-center text-sm font-medium ${
                  currentStep >= step.number
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-200 text-gray-600'
                }`}
              >
                {step.number}
              </div>
              <div className="mt-2 text-xs text-center max-w-20">
                <span
                  className={`${
                    currentStep >= step.number ? 'text-blue-600' : 'text-gray-500'
                  }`}
                >
                  {step.title}
                </span>
              </div>
            </div>
            {index < steps.length - 1 && (
              <div
                className={`flex-1 h-1 mx-4 ${
                  currentStep > step.number ? 'bg-blue-600' : 'bg-gray-200'
                }`}
              />
            )}
          </React.Fragment>
        ))}
      </div>
    </div>
  );
};

// Step renderer component
const StepRenderer: React.FC = () => {
  const { currentStep } = useForm();

  switch (currentStep) {
    case 1:
      return <Step1NetworkSelection />;
    case 2:
      return <Step2DataPlanSelection />;
    case 3:
      return <Step3ReviewConfirm />;
    case 4:
      return <Step4PaymentStatus />;
    default:
      return <Step1NetworkSelection />;
  }
};

// Main app content
const AppContent: React.FC = () => {
  const { currentStep } = useForm();

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
            Crypto to Data Purchase
          </h1>
          <p className="text-gray-600">
            Buy data plans with cryptocurrency quickly and securely
          </p>
        </div>

        {/* Progress Indicator */}
        <ProgressIndicator currentStep={currentStep} />

        {/* Step Content */}
        <div className="bg-white rounded-lg shadow-sm p-8">
          <StepRenderer />
        </div>

        {/* Footer */}
        <div className="text-center mt-8 text-sm text-gray-500">
          <p>Secure • Fast • Reliable</p>
        </div>
      </div>
    </div>
  );
};

// Main App component
const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <FormProvider>
        <AppContent />
      </FormProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export default App;
