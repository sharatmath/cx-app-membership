package com.dev.prepaid.util;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.dev.prepaid.model.invocation.InvocationRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RESTUtil {
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity getData(InvocationRequest invocation, String token,String host, Object object, Class c, String contentType) {
    	ResponseEntity status = restServiceExchange(
                RequestUtil.getClientJWTHttpInterceptor(invocation, contentType, token),
        		host,
                HttpMethod.GET,
                new HttpEntity<>(object),
                c);
        return status;
    }
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity getExportData(InvocationRequest invocation, String token,String host, Object object, Class c, String contentType) {
    	ResponseEntity status = restServiceExchange(
                RequestUtil.getClientJWTHttpInterceptor(invocation, contentType, token),
        		host,
                HttpMethod.GET,
                new HttpEntity<>(object),
                c);
        return status;
    }
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity productImportPost(InvocationRequest invocation, String token,String host, Object object, Class c, String contentType) {
		ResponseEntity status = restServiceExchange(
	            RequestUtil.getClientJWTHttpInterceptor(invocation, contentType, token),
	    		host,
	            HttpMethod.POST,
	            new HttpEntity<>(object),
	            c);
	    return status;
	}
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity onCompletionCallbackPatch(InvocationRequest invocation, String token,String host, Object object, Class c, String contentType) {
    	ResponseEntity status = restServiceExchange(
                RequestUtil.getClientJWTHttpInterceptor(invocation, contentType, token),
        		host,
                HttpMethod.PATCH,
                new HttpEntity<>(object),
                c);
        return status;
    }
		
	@SuppressWarnings("rawtypes")
	public static ResponseEntity onCompletionCallbackPost(InvocationRequest invocation, String token,String host, Object object, Class c, String contentType) {
    	ResponseEntity status = restServiceExchange(
                RequestUtil.getClientJWTHttpInterceptor(invocation, contentType, token),
        		host,
                HttpMethod.POST,
                new HttpEntity<>(object),
                c);
        return status;
    }
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity getDataOrds(String host, Object object, Class c, String contentType) {
    	ResponseEntity status = restServiceExchange(
                RequestUtil.getClientHttpInterceptor(contentType),
        		host,
                HttpMethod.GET,
                new HttpEntity<>(object),
                c);
        return status;
    }
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity postData(String user,String pass,String host, Object object, Class c, String contentType) {
    	log.debug("post host : "+host);
    	ResponseEntity status = restServiceExchange(
                RequestUtil.getClientHttpInterceptor(contentType),
        		host,
                HttpMethod.POST,
                new HttpEntity<>(object),
                c);
        return status;
    }
    
    @SuppressWarnings("rawtypes")
	public static HttpStatus updateData(String user,String pass,String host, Object object, Class c, String contentType) {
    	log.debug("put host : "+host);
        HttpStatus status = restServiceExchange(
                RequestUtil.getClientHttpInterceptor(contentType),
        		host,
                HttpMethod.PUT,
                new HttpEntity<>(object),
                String.class).getStatusCode();
        return status;
    }
    
    @SuppressWarnings("rawtypes")
	public static HttpStatus deleteData(String user,String pass,String host, Object object, Class c, String contentType) {
    	log.debug("delete host : "+host);
        HttpStatus status = restServiceExchange(
                RequestUtil.getClientHttpInterceptor(contentType),
        		host,
                HttpMethod.DELETE,
                null,
                c,
                object).getStatusCode();
        return status;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseEntity restServiceExchange(ArrayList<ClientHttpRequestInterceptor> interceptors, String host, HttpMethod httpMethod, HttpEntity request, Class c, Object... varargs) {
    	RestTemplate restTemplate = new RestTemplate(); 
    	if (httpMethod.equals(HttpMethod.PUT) || httpMethod.equals(HttpMethod.DELETE)) {
            c = null;
        }
    	
    	if (httpMethod.equals(HttpMethod.PATCH)) {
    		HttpClient httpClient = HttpClientBuilder.create().build();
    		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
    		factory.setConnectTimeout(5000);
    		factory.setReadTimeout(5000);
    		restTemplate.setRequestFactory(factory);
    	}
                	    
	    restTemplate.setInterceptors(interceptors);        
	    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	    	        
	    
        return restTemplate.exchange(host, httpMethod, request, c, varargs);
    }
}
