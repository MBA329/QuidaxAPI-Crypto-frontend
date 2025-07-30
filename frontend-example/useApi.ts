// src/hooks/useApi.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { apiService, PurchaseRequest } from '../lib/api';

// Query Keys
export const queryKeys = {
  networks: ['networks'] as const,
  cryptoCurrencies: ['cryptoCurrencies'] as const,
  buyPrice: (pair: string) => ['buyPrice', pair] as const,
  dataPlans: (network: string) => ['dataPlans', network] as const,
};

// Custom Hooks
export const useNetworks = () => {
  return useQuery({
    queryKey: queryKeys.networks,
    queryFn: () => apiService.getNetworks().then(res => res.data),
    staleTime: 5 * 60 * 1000, // 5 minutes
  });
};

export const useCryptoCurrencies = () => {
  return useQuery({
    queryKey: queryKeys.cryptoCurrencies,
    queryFn: () => apiService.getCryptoCurrencies().then(res => res.data),
    staleTime: 5 * 60 * 1000,
  });
};

export const useBuyPrice = (pair: string, enabled: boolean = true) => {
  return useQuery({
    queryKey: queryKeys.buyPrice(pair),
    queryFn: () => apiService.getBuyPrice(pair).then(res => res.data),
    enabled: enabled && !!pair,
    refetchInterval: 30000, // Refetch every 30 seconds
    staleTime: 25000, // Consider stale after 25 seconds
  });
};

export const useDataPlans = (network: string, enabled: boolean = true) => {
  return useQuery({
    queryKey: queryKeys.dataPlans(network),
    queryFn: () => apiService.getDataPlans(network).then(res => res.data),
    enabled: enabled && !!network,
    staleTime: 10 * 60 * 1000, // 10 minutes
  });
};

export const usePurchaseData = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (data: PurchaseRequest) => 
      apiService.purchaseData(data).then(res => res.data),
    onSuccess: () => {
      // Invalidate and refetch any related queries if needed
      queryClient.invalidateQueries({ queryKey: ['purchases'] });
    },
  });
};

export const useVerifyPurchase = () => {
  return useMutation({
    mutationFn: (requestId: string) => 
      apiService.verifyPurchase(requestId).then(res => res.data),
  });
};
