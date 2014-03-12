package com.personal.rents.webservice.response;

/**
 * Status codes that are used to construct/verify web service responses
 */
public enum WebserviceResponseStatus {
	OK(200),
	PARTIAL_CONTENT(206),
	OPERATION_IN_PROGRESS(210),
	OPERATION_FINISHED(212),
	UNAUTHORIZED(401),
	FORBIDDEN(403),
	INVALID_DATA(420),
	ACCOUNT_CONFLICT(421),
	BAD_CREDENTIALS(422),
	VERSION_OUTDATED(423),
	SERVER_ERROR(500),
	OPERATION_FAILED(510);
	
	private int code;
	
	private WebserviceResponseStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}