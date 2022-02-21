package com.dev.prepaid;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestHashMap {

    @Test
    public  void Test1(){
        Map<String, Integer> c = new HashMap<>();
        String rowId = "eventCondition=54f3bdb6-8bf5-400e-aa15-3b94a3b3643e=CA62AC3FC629B95AF3F3B2493795126E76CFFDAE";
        System.out.println(getTrnLogId(rowId));


    }

    private String getTrnLogId(String rowId){
        if(rowId != null ){
            if(rowId.contains("eventCondition")) {
                String[] data = rowId.split("=");
                return data[2];
            }
            return rowId;
        }else {
            return "";
        }
    }
}
