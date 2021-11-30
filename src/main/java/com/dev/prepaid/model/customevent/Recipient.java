package com.dev.prepaid.model.customevent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipient {
    private String customerId;
    private String emailAddress;
    private ListName listName;
    private String recipientId;
    private String mobileNumber;
    private String emailFormat;
}
