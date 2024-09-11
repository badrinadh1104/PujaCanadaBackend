package com.dto;

public class ApiResponse {
	private Boolean success;
	private String message;
	public ApiResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
}
