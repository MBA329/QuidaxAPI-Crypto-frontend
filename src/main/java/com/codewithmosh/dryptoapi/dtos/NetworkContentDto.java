package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class NetworkContentDto {
    @SerializedName("ServiceName")
    private String serviceName;

    @SerializedName("serviceID")
    private String serviceId;

    @SerializedName("convinience_fee")
    private String convenienceFee;

    @SerializedName("variations")
    private List<Variation> variations;

    @Data
    public static class Variation {
        @SerializedName("variation_code")
        private String variationCode;

        @SerializedName("name")
        private String name;

        @SerializedName("variation_amount")
        private String variationAmount;

        @SerializedName("fixedPrice")
        private String fixedPrice;
    }
}

