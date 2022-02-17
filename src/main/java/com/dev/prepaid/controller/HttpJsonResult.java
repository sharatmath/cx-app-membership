package com.dev.prepaid.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpJsonResult<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean success = true;

	private Date systemTime;

	public Date getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
	}

	private T data;

	private String message;

	private String errorCode;

	private Integer totalCount = 0;

	public HttpJsonResult() {
		this.message = "";
		this.errorCode = "";
	}

	public HttpJsonResult(T data) {
		this.data = data;
		this.message = "";
		this.errorCode = "";
		this.systemTime = new Date();
	}

	public HttpJsonResult(String errorMessage) {
		this.success = false;
		this.message = errorMessage;
	}

	public void setError(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.message = errorMessage;
		this.success = false;
	}

	public Boolean getSuccess() {
		return this.success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setTotalCount(Integer count) {
		this.totalCount = count;
	}

	public Integer getTotalCount() {
		return this.totalCount;
	}

	public static <T> T convertStreamToEntity(HttpServletRequest request, Class<T> type) throws ServletException {
		ServletInputStream in = null;
		ObjectInputStream obj = null;
		try {
			in = request.getInputStream();
			obj = new ObjectInputStream(in);
			return (T) obj.readObject();
		} catch (Exception e) {
			log.error("HttpJsonResult convertStreamToEntity Error", e);
		} finally {
			if (obj != null) {
				try {
					obj.close();
				} catch (IOException e) {
					log.error("HttpJsonResult convertStreamToEntity Error", e);
				}
			}
		}
		return null;
	}

}