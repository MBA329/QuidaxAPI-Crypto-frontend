package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VTPassPurchaseResponse {
    @SerializedName("requestId")
    private String requestId;

    @SerializedName("content")
    private Content content;

    @Data
    public static class Content {
        @SerializedName("transactions")
        private Transaction transactions;
    }

    @Data
    public static class Transaction {
        @SerializedName("status")
        private String status;
    }
}
