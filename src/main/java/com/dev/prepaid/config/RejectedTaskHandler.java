package com.dev.prepaid.config;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedTaskHandler implements RejectedExecutionHandler 
{ 
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) 
    { 
//        System.out.printf("RejectedTaskHandler: The task %s has been rejected", r.toString()); 
    }
}
