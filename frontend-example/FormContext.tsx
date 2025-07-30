// src/context/FormContext.tsx
import React, { createContext, useContext, useState, ReactNode } from 'react';

export interface FormData {
  // Step 1
  network: string;
  cryptoCurrency: string;
  
  // Step 2
  dataPlan: string;
  phoneNumber: string;
  
  // Step 3 (calculated)
  amount?: number;
  cryptoAmount?: number;
  
  // Step 4 (result)
  requestId?: string;
  walletAddress?: string;
  qrCodeUrl?: string;
}

interface FormContextType {
  currentStep: number;
  formData: FormData;
  updateFormData: (data: Partial<FormData>) => void;
  nextStep: () => void;
  prevStep: () => void;
  resetForm: () => void;
}

const FormContext = createContext<FormContextType | undefined>(undefined);

const initialFormData: FormData = {
  network: '',
  cryptoCurrency: '',
  dataPlan: '',
  phoneNumber: '',
};

export const FormProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState<FormData>(initialFormData);

  const updateFormData = (data: Partial<FormData>) => {
    setFormData(prev => ({ ...prev, ...data }));
  };

  const nextStep = () => {
    setCurrentStep(prev => Math.min(prev + 1, 4));
  };

  const prevStep = () => {
    setCurrentStep(prev => Math.max(prev - 1, 1));
  };

  const resetForm = () => {
    setCurrentStep(1);
    setFormData(initialFormData);
  };

  return (
    <FormContext.Provider value={{
      currentStep,
      formData,
      updateFormData,
      nextStep,
      prevStep,
      resetForm,
    }}>
      {children}
    </FormContext.Provider>
  );
};

export const useForm = () => {
  const context = useContext(FormContext);
  if (context === undefined) {
    throw new Error('useForm must be used within a FormProvider');
  }
  return context;
};
