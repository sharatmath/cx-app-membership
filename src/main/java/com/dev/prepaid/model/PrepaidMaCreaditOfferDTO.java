package com.dev.prepaid.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidMaCreaditOfferDTO {

    @SerializedName("id")private Long id;
    @SerializedName("productName")private String productName;
    @SerializedName("idescription")private String description;
    @SerializedName("value")private Float value;
    @SerializedName("validity")private Long validity;
    @SerializedName("startDate")private Date startDate;
    @SerializedName("endDate")private Date endDate;
    @SerializedName("createdBy")private String createdBy;
    @SerializedName("createdDate")private Date createdDate;
    @SerializedName("lastMoodifiedBy")private String lastModifiedBy;
    @SerializedName("lastModifiedDate")private Date lastModifiedDate;
    @SerializedName("deletedBy")private String deletedBy;
    @SerializedName("deletedDate")private Date deletedDate;
    @SerializedName("action")private String action;
}
