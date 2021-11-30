package com.dev.prepaid.service;

import com.dev.prepaid.model.customevent.CustomEventDto;

public interface CustomEventService {
    public void invokeCustomEvent(CustomEventDto dto);
}
