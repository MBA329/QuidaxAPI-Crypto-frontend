package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VTPassQueryTransactionResponse {
    @SerializedName("requestId")
    private String requestId;

    @SerializedName("content")
    private Content content;

    @Data
    public static class Content {
        @SerializedName("transaction")
        private Transaction transaction;
    }

    @Data
    public static class Transaction {
        @SerializedName("status")
        private String status;
    }
}
