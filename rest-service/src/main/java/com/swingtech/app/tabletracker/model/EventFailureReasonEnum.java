package com.swingtech.app.tabletracker.model;

public enum EventFailureReasonEnum {
	SYSTEM_ERROR (500, "Unexpected system error occured"),
	CURRENT_EVENT_EXISTS_FOR_START_LOGGING(500, "Trying to log a start event, but one already exists"),
	NO_CURRENT_EVENT_FOR_END_LOGGING(500, "Trying to log an end event, but there was no current event to log to"),
	INVALID_START_EVENT_REQUEST(400, "The request is invalid for a start event"),
	INVALID_END_EVENT_REQUEST(400, "The request is invalid for an end event"),
	INVALID_CURRENT_EVENT_FOR_END_EVENT_REQUEST(500, "Trying to log an end event but the current state is invalid");
	
    private final int httpStatusCode;   // in kilograms
    private final String errorString; // in meters
    
    EventFailureReasonEnum(int httpStatusCode, String errorString) {
    	this.httpStatusCode = httpStatusCode;
    	this.errorString = errorString;
    }

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getErrorString() {
		return errorString;
	}
}
