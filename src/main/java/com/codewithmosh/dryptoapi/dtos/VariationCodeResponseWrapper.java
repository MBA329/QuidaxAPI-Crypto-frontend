package com.codewithmosh.dryptoapi.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VariationCodeResponseWrapper {
    @SerializedName("response_description")
    private String responseDescription;

    @SerializedName("content")
    private NetworkContentDto content;
}
