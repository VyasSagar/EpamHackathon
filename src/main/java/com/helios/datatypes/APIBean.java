package com.helios.datatypes;

import java.util.HashMap;
import java.util.Map;

public class APIBean {

    private String method;
    private String url;
    private String requestPayload;
    private String expectedResponse = "";
    private String actualResponse = "";
    private String responseCode;
    private String responseTime;
    private String message;
    private String result;
    private String contentType;
    private Map<String, String > headers = new HashMap<>();
    
    public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public String getActualResponse() {
        return actualResponse;
    }

    public void setActualResponse(String actualResponse) {
        if(!actualResponse.equals(expectedResponse)) result = "FAIL";
        else result = "PASS";
        this.actualResponse = actualResponse;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    
    
    public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "APIBean [method=" + method + ", url=" + url + ", requestPayload=" + requestPayload
				+ ", expectedResponse=" + expectedResponse + ", actualResponse=" + actualResponse + ", responseCode="
				+ responseCode + ", responseTime=" + responseTime + ", message=" + message + ", result=" + result
				+ ", contentType=" + contentType + ", headers=" + headers + "]";
	}
	

	
}
