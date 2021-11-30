package com.dev.prepaid.model.customevent;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEventDto {
    CustomEvent customEvent;
    List<RecipientData> recipientData;
}
