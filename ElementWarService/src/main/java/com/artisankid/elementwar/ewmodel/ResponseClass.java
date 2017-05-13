package com.artisankid.elementwar.ewmodel;

import java.util.Date;

public class ResponseClass<T> {

	private Double responseTime;
	private Integer  code;
	private String message;
	private T data;
	
	public ResponseClass() {
		responseTime = new Date().getTime() / 1000.;
		code = 0;
		message = "success";
	}
	
	public Double getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Double responseTime) {
		this.responseTime = responseTime;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
