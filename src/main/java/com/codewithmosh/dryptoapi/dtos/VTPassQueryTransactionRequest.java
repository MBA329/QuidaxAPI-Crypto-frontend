package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VTPassQueryTransactionRequest {
    @SerializedName("request_id")
    private String requestId;
}
