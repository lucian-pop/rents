package com.personal.rents.webservice.response;

/**
 * Status codes that are used to construct web service responses
 */
public enum WebserviceResponseStatus {
	OK(200),
	PARTIAL_CONTENT(206),
	OPERATION_IN_PROGRESS(210),
	OPERATION_STOPPED(211),
	OPERATION_FINISHED(212),
	UNAUTHORIZED(401),
	FORBIDDEN(403),
	INVALID_DATA(420),
	SERVER_ERROR(500);
	
	private int code;
	
	private WebserviceResponseStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}