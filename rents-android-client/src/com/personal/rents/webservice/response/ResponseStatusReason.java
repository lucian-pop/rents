package com.personal.rents.webservice.response;

public enum ResponseStatusReason {
	OK,
	NETWORK_ERROR,
	NETWORK_DOWN_ERROR,
	NETWORK_UNREACHABLE_ERROR,
	UNAUTHORIZED_ERROR,
	OPERATION_FAILED_ERROR,
	ACCOUNT_CONFLICT_ERROR,
	BAD_CREDENTIALS_ERROR,
	VERSION_OUTDATED_ERROR,
	UNKNOWN_ERROR;
}
