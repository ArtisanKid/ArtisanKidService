package com.artisankid.elementwar.ewmodel;

import java.util.Date;

public class ResponseClass<T> {

	private double responseTime;
	private int  code;
	private String message;
	private T data;
	
	public ResponseClass() {
		responseTime = new Date().getTime() / 1000.;
		code = 0;
		message = "success";
	}
	
	public double getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
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
