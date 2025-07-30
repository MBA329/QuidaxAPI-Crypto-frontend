#!/bin/bash
# QuidaxAPI-Crypto Test Script
# Make sure your application is running on localhost:8080

BASE_URL="http://localhost:8080"

echo "🚀 Testing QuidaxAPI-Crypto Endpoints"
echo "======================================"

# 1. Health Check
echo -e "\n1. Testing Health Check..."
curl -X GET "$BASE_URL/message/hello" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

# 2. Get Available Networks
echo -e "\n2. Getting Available Networks..."
curl -X GET "$BASE_URL/purchase/networks" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

# 3. Get Available Cryptocurrencies
echo -e "\n3. Getting Available Cryptocurrencies..."
curl -X GET "$BASE_URL/purchase/crypto-currencies" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

# 4. Get Crypto Buy Price (BTC/NGN)
echo -e "\n4. Getting BTC Buy Price..."
curl -X GET "$BASE_URL/purchase/buy-price/btcngn" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

# 5. Fetch Data Plans for MTN
echo -e "\n5. Fetching MTN Data Plans..."
curl -X POST "$BASE_URL/purchase/fetch-plans/mtn" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

# 6. Test Buy Data (This will likely fail without proper setup)
echo -e "\n6. Testing Buy Data Request..."
curl -X POST "$BASE_URL/purchase/buy-data" \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "08012345678",
    "network": "mtn",
    "billersCode": "08012345678",
    "dataPlanCode": "mtn-data-500mb-30days",
    "cryptoCurrency": "btc"
  }' \
  -w "\nStatus: %{http_code}\n" \
  -s

echo -e "\n✅ Testing completed!"
echo "Note: Some endpoints may fail if external APIs (Quidax/VTPass) are not properly configured."
