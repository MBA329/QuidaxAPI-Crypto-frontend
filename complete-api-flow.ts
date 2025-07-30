// Complete API Synchronization Flow Implementation

class CryptoUtilityAPI {
  private baseURL = 'http://localhost:8080';
  private pollInterval = 5000; // 5 seconds
  private maxPollAttempts = 360; // 30 minutes total

  // Phase 1: Initialize and gather data
  async initializePurchase() {
    try {
      // Step 1: Get available options in parallel
      const [networks, cryptos] = await Promise.all([
        this.getNetworks(),
        this.getCryptoCurrencies()
      ]);

      return { networks, cryptos };
    } catch (error) {
      throw new Error('Failed to initialize purchase options');
    }
  }

  // Step 2: Get real-time price with auto-refresh
  async getCryptoPriceWithRefresh(cryptoPair: string) {
    const price = await this.getBuyPrice(cryptoPair);
    
    // Set up real-time price updates every 30 seconds
    const priceInterval = setInterval(async () => {
      try {
        const updatedPrice = await this.getBuyPrice(cryptoPair);
        this.onPriceUpdate(updatedPrice);
      } catch (error) {
        console.error('Price update failed:', error);
      }
    }, 30000);

    return { price, priceInterval };
  }

  // Step 3: Get data plans for network
  async getDataPlansForNetwork(network: string) {
    try {
      const plans = await this.getDataPlans(network);
      return plans;
    } catch (error) {
      throw new Error(`Failed to fetch data plans for ${network}`);
    }
  }

  // Phase 2: Execute purchase with validation
  async executePurchase(purchaseData: PurchaseRequest) {
    try {
      // Validate all required fields
      this.validatePurchaseRequest(purchaseData);

      // Get latest price before purchase
      const cryptoPair = `${purchaseData.cryptoCurrency}ngn`;
      const currentPrice = await this.getBuyPrice(cryptoPair);

      // Calculate expected crypto amount
      const expectedCryptoAmount = this.calculateCryptoAmount(
        purchaseData.dataPlanAmount, 
        currentPrice.data.ticker.buy
      );

      // Execute purchase
      const purchaseResponse = await this.purchaseData(purchaseData);

      // Start payment monitoring
      const verificationPromise = this.startPaymentVerification(
        purchaseResponse.requestId
      );

      return {
        purchaseResponse,
        expectedCryptoAmount,
        verificationPromise
      };
    } catch (error) {
      throw new Error(`Purchase execution failed: ${error.message}`);
    }
  }

  // Phase 3: Monitor payment with exponential backoff
  async startPaymentVerification(requestId: string): Promise<VerificationResult> {
    return new Promise((resolve, reject) => {
      let attempts = 0;
      let currentInterval = this.pollInterval;

      const pollStatus = async () => {
        try {
          attempts++;
          const status = await this.verifyPurchase(requestId);

          // Check completion states
          if (status.status === 'COMPLETED') {
            resolve({
              status: 'SUCCESS',
              message: 'Data delivered successfully',
              attempts
            });
            return;
          }

          if (status.status === 'FAILED' || status.status === 'EXPIRED') {
            resolve({
              status: 'FAILED',
              message: status.message,
              attempts
            });
            return;
          }

          // Continue polling for PENDING/PAID states
          if (attempts >= this.maxPollAttempts) {
            resolve({
              status: 'TIMEOUT',
              message: 'Verification timeout reached',
              attempts
            });
            return;
          }

          // Schedule next poll with exponential backoff
          setTimeout(pollStatus, this.getNextPollInterval(attempts));

        } catch (error) {
          if (attempts >= 3) {
            reject(new Error(`Verification failed after ${attempts} attempts`));
          } else {
            // Retry on error
            setTimeout(pollStatus, currentInterval);
          }
        }
      };

      // Start polling
      pollStatus();
    });
  }

  // Helper: Calculate next poll interval with exponential backoff
  private getNextPollInterval(attempts: number): number {
    // Start at 5s, max at 60s
    return Math.min(this.pollInterval * Math.pow(1.5, Math.floor(attempts / 10)), 60000);
  }

  // Helper: Validate purchase request
  private validatePurchaseRequest(data: PurchaseRequest): void {
    const required = ['phoneNumber', 'network', 'cryptoCurrency', 'dataPlanCode'];
    const missing = required.filter(field => !data[field]);
    
    if (missing.length > 0) {
      throw new Error(`Missing required fields: ${missing.join(', ')}`);
    }

    // Validate phone number format
    const phoneRegex = /^(\+234|0)[789][01]\d{8}$/;
    if (!phoneRegex.test(data.phoneNumber)) {
      throw new Error('Invalid Nigerian phone number format');
    }
  }

  // API Methods
  private async getNetworks() {
    const response = await fetch(`${this.baseURL}/purchase/networks`);
    return response.json();
  }

  private async getCryptoCurrencies() {
    const response = await fetch(`${this.baseURL}/purchase/crypto-currencies`);
    return response.json();
  }

  private async getBuyPrice(cryptoPair: string) {
    const response = await fetch(`${this.baseURL}/purchase/buy-price/${cryptoPair}`);
    return response.json();
  }

  private async getDataPlans(network: string) {
    const response = await fetch(`${this.baseURL}/purchase/fetch-plans/${network}`, {
      method: 'POST'
    });
    return response.json();
  }

  private async purchaseData(data: PurchaseRequest) {
    const response = await fetch(`${this.baseURL}/purchase/buy-data`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return response.json();
  }

  private async verifyPurchase(requestId: string) {
    const response = await fetch(`${this.baseURL}/purchase/verify/${requestId}`, {
      method: 'POST'
    });
    return response.json();
  }

  private calculateCryptoAmount(nairaAmount: number, cryptoPrice: number): number {
    return nairaAmount / cryptoPrice;
  }

  private onPriceUpdate(newPrice: any): void {
    // Emit event or update UI with new price
    console.log('Price updated:', newPrice);
  }
}

// Usage Example
const api = new CryptoUtilityAPI();

async function completePurchaseFlow() {
  try {
    // Phase 1: Initialize
    const { networks, cryptos } = await api.initializePurchase();
    console.log('Available options:', { networks, cryptos });

    // User selects network and crypto
    const selectedNetwork = 'mtn';
    const selectedCrypto = 'btc';

    // Get real-time price
    const { price, priceInterval } = await api.getCryptoPriceWithRefresh(`${selectedCrypto}ngn`);
    
    // Get data plans
    const dataPlans = await api.getDataPlansForNetwork(selectedNetwork);
    console.log('Available plans:', dataPlans);

    // User selects data plan and enters phone
    const purchaseRequest = {
      phoneNumber: '08012345678',
      network: selectedNetwork,
      cryptoCurrency: selectedCrypto,
      dataPlanCode: 'mtn-data-1gb',
      billersCode: null
    };

    // Phase 2: Execute purchase
    const { purchaseResponse, verificationPromise } = await api.executePurchase(purchaseRequest);
    console.log('Purchase initiated:', purchaseResponse);

    // Phase 3: Monitor payment
    const result = await verificationPromise;
    console.log('Final result:', result);

    // Cleanup
    clearInterval(priceInterval);

  } catch (error) {
    console.error('Purchase flow failed:', error);
  }
}

// TypeScript Interfaces
interface PurchaseRequest {
  phoneNumber: string;
  network: string;
  cryptoCurrency: string;
  dataPlanCode: string;
  billersCode?: string;
}

interface VerificationResult {
  status: 'SUCCESS' | 'FAILED' | 'TIMEOUT';
  message: string;
  attempts: number;
}
