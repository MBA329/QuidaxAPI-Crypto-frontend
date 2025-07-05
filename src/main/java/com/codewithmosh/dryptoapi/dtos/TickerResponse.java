package com.codewithmosh.dryptoapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TickerResponse {
    @SerializedName("data")
    private TickerDataWrapper data;

    @Data
    public static class TickerDataWrapper {
        @SerializedName("ticker")
        private TickerData ticker;
    }

    @Data
    public static class TickerData {
        @SerializedName("buy")
        private String buy;

        @SerializedName("sell")
        private String sell;

        @SerializedName("last")
        private String last_price;
    }
}
