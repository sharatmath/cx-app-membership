package com.dev.prepaid.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import oracle.nosql.driver.NoSQLHandle;

@Data
@Slf4j
public class NosqlPool {
	private GenericObjectPool<NoSQLHandle> pool;

    public NosqlPool(NosqlFactory factory) {
        this.pool = new GenericObjectPool<>(factory, factory.getProperties().getPool());
    }

    public NoSQLHandle borrowObject() throws Exception {       	
    	log.debug("\npool idle : {} \npool active : {} \nthread waiting : {}"
    			,pool.getNumIdle() 
    			,pool.getNumActive()
    			,pool.getNumWaiters());       	  	
    	log.debug("borrow handler");
        try {
        	pool.setTestOnBorrow(true);
        	pool.setTestOnCreate(true);   
        	return pool.borrowObject();
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new Exception(e.getMessage());
        }
    }

    public void returnObject(NoSQLHandle handle) {   	
    	log.debug("return handler");
        if (handle!=null) {
            pool.returnObject(handle);
        }
    }
}
