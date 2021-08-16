package com.dev.prepaid.type;

import java.util.HashMap;
import java.util.Map;

public enum EventType {

    TOPUP("Top-Up"),
    USAGE("Usage"),
    ARPU("ARPU");

    private String description;

    EventType(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }

    //Lookup table
    private static final Map<String, EventType> lookup = new HashMap<>();
    //Populate the lookup table on loading time
    static
    {
        for(EventType env : EventType.values())
        {
            lookup.put(env.getDescription(), env);
        }
    }
    public static EventType get(String description){
        return  lookup.get(description);
    }
}
