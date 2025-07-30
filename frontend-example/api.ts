// src/lib/api.ts
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Response Types
export interface TickerResponse {
  bid: number;
  ask: number;
  last: number;
}

export interface DataPlan {
  variation_code: string;
  name: string;
  variation_amount: string;
  fixedPrice: string;
}

export interface PurchaseRequest {
  phoneNumber: string;
  network: string;
  cryptoCurrency: string;
  dataPlanCode: string;
}

export interface PurchaseResponse {
  requestId: string;
  status: string;
  amount: number;
  walletAddress: string;
  qrCodeUrl?: string;
}

// API Functions
export const apiService = {
  // Get available networks
  getNetworks: () => 
    apiClient.get<string[]>('/purchase/networks'),

  // Get available crypto currencies  
  getCryptoCurrencies: () =>
    apiClient.get<string[]>('/purchase/crypto-currencies'),

  // Get crypto buy price
  getBuyPrice: (pair: string) =>
    apiClient.get<TickerResponse>(`/purchase/buy-price/${pair}`),

  // Get data plans for network
  getDataPlans: (network: string) =>
    apiClient.post<DataPlan[]>(`/purchase/fetch-plans/${network}`),

  // Purchase data
  purchaseData: (data: PurchaseRequest) =>
    apiClient.post<PurchaseResponse>('/purchase/buy-data', data),

  // Verify purchase
  verifyPurchase: (requestId: string) =>
    apiClient.post(`/purchase/verify/${requestId}`),
};
