package com.dev.prepaid.type;

import java.util.HashMap;
import java.util.Map;

public enum ProvisionType {

    DIRECT_PROVISION("directProvisioning"),
    OFFER_MONITORING_WITH_OFFER_ASSIGNMENT("offerMonitoringWithAssignment"),
    EVENT_CONDITION_WITH_DIRECT_PROVISION("eventConditionDirectProvisioning"),
    EVENT_CONDITION("eventCondition");

    private String description;

    ProvisionType(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }

    private static final Map<String, ProvisionType> lookup = new HashMap<>();

    static
    {
        for(ProvisionType env : ProvisionType.values())
        {
            lookup.put(env.getDescription(), env);
        }
    }

    public static ProvisionType get(String description){
        return  lookup.get(description);
    }
}
