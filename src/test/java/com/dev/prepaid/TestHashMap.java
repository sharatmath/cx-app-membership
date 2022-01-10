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
        System.out.println(c.get("a"));
    }
}
