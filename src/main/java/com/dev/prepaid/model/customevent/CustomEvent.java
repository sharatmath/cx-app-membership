package com.dev.prepaid.model.customevent;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEvent {
    private String eventNumberDataMapping;
    private String eventDateDataMapping;
    private String eventStringDataMapping;
}
