package com.dev.prepaid.model.invocation;

import java.util.HashMap;
import java.util.Map;

public class Headers {
	Map<String, String> headers = new HashMap<>();

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

}
