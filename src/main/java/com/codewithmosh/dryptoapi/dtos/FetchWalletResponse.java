package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FetchWalletResponse {
    @SerializedName("data")
    private WalletData data;

    @Data
    public static class WalletData {
        @SerializedName("currency")
        private String currency;

        @SerializedName("address")
        private String depositAddress;
    }
}
