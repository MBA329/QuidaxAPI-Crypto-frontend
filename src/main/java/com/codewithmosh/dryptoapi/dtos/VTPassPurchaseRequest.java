package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VTPassPurchaseRequest {
    @SerializedName("request_id")
    private String requestId;

    @SerializedName("serviceID")
    private String serviceID; // "airtel-data"

    @SerializedName("billersCode")
    private String billersCode; // recipient phone number

    @SerializedName("variation_code")
    private String variationCode;

    @SerializedName("phone")
    private String phone; // customer's phone number
//
//    @SerializedName("email")
//    private String email; // optional
}

