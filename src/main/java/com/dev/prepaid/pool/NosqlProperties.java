package com.dev.prepaid.pool;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
//import oracle.nosql.driver.NoSQLHandle;

@Data
@ConfigurationProperties(prefix = "nosql")
public class NosqlProperties {
	//nosql config
	@Value("${user.id}")    
    private String userId;
    @Value("${tenant.id}")    
    private String tenantId;
    @Value("${fingerprint}")    
    private String fingerprint;
    @Value("${pem.key}")    
    private String pathKey;    
    private String key;
    @Value("${endpoint}")    
    private String endpoint;
    
    //pool config
	@Value("${nosql.pool.max-total}")
	private int maxTotal;
	@Value("${nosql.pool.max-idle}")
	private int maxIdle;
	@Value("${nosql.pool.min-idle}")
	private int minIdle;

	private Pool pool = new Pool(maxTotal,maxIdle,minIdle);

	public String getKey() {
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get(pathKey)), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
    
    public static class Pool extends GenericObjectPoolConfig<NoSQLHandle> {

        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;
        
        public Pool(int maxTotal, int maxIdle, int minIdle) {
			super();
			this.maxTotal = maxTotal;
			this.maxIdle = maxIdle;
			this.minIdle = minIdle;
		}

        public Pool() {
            super();
        }
        @Override
        public int getMaxTotal() {
            return maxTotal;
        }
        @Override
        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }
        @Override
        public int getMaxIdle() {
            return maxIdle;
        }
        @Override
        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }
        @Override
        public int getMinIdle() {
            return minIdle;
        }
        @Override
        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

    }

}
