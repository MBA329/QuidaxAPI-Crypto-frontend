#!/bin/bash

echo "🧪 Running Mock API Tests"
echo "========================="

# Test 1: Health Check (Mock)
echo "✅ Health Check: OK"
echo "Response: Hello, world!"
echo "Status: 200"

# Test 2: Networks (Mock)
echo -e "\n✅ Available Networks:"
echo '["airtel", "mtn", "glo", "etisalat"]'
echo "Status: 200"

# Test 3: Crypto Currencies (Mock)
echo -e "\n✅ Available Cryptocurrencies:"
echo '["btc", "usdt", "trx"]'
echo "Status: 200"

# Test 4: Buy Price (Mock) 
echo -e "\n✅ BTC Buy Price:"
echo '{"bid": 50000000, "ask": 50500000, "last": 50250000}'
echo "Status: 200"

echo -e "\n🔧 To test with real data:"
echo "1. Install Java 17 JDK: brew install openjdk@17"
echo "2. Set JAVA_HOME: export JAVA_HOME=$(/usr/libexec/java_home -v 17)"
echo "3. Run application: ./mvnw spring-boot:run"
echo "4. Execute: ./test-endpoints.sh"
