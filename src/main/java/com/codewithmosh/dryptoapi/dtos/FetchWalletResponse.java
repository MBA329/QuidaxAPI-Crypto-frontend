package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FetchWalletResponse {
    @SerializedName("data")
    private WalletData data;

    @Data
    public static class WalletData {
        @SerializedName("id")
        private String id;

        @SerializedName("currency")
        private String cryptoCurrency;

        @SerializedName("address")
        private String depositAddress;

        @SerializedName("address")
        private String network;
    }
}
