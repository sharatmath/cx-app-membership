package com.dev.prepaid.type;

import java.util.HashMap;
import java.util.Map;

public enum UsageServiceType {
    DATA("Data"),
    SMS("SMS"),
    IDD("IDD"),
    VOICE("Voice");

    private String description;

    UsageServiceType(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }

    private static final Map<String, UsageServiceType> lookup = new HashMap<>();

    static
    {
        for(UsageServiceType env : UsageServiceType.values())
        {
            lookup.put(env.getDescription(), env);
        }
    }

    public static UsageServiceType get(String description){
        return  lookup.get(description);
    }

}
