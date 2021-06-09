package com.dev.prepaid.util;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
	
	private static final Gson gson = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .create();
	
    public static String deserializeObjectToJSON(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> Object serializeObjectFromJSON(String json, Class<T> classType) {
        return gson.fromJson(json, classType);
    }

    public static <T> List<T> serializeListOfObjectsFromJSON(String json, Type listType) {
        return gson.fromJson(json, listType);
    }
    
}
