package com.dev.prepaid.type;

import java.util.HashMap;
import java.util.Map;

public enum OperatorType {
    LESS_THAN("Less than"),
    MORE_THAN("More than"),
    EQUAL_TO("Equal to"),
    LESS_THAN_OR_EQUAL_TO("Less than or equal to"),
    MORE_THAN_OR_EQUAL_TO("More than or equal to");

    private String description;

    OperatorType(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }

    private static final Map<String, OperatorType> lookup = new HashMap<>();

    static
    {
        for(OperatorType env : OperatorType.values())
        {
            lookup.put(env.getDescription(), env);
        }
    }

    public static OperatorType get(String description){
        return  lookup.get(description);
    }

}
