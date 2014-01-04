package com.personal.rents.rest.util;

public enum RetrofitResponseStatus {

	OK(""),
	NETWORK_DOWN_ERROR("Conectarea la serverele noastre a esuat. Asteptati cateva momente si" +
			" incercati din nou."),
	NETWORK_UNREACHABLE_ERROR("Conectarea la internet a esuat. Activati internetul si incercati" +
			" din nou."),
	UKNOWN_ERROR("Operatia nu a putut fi finalizata. Va rog incercati din nou.");

	private String message;

	private RetrofitResponseStatus(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
