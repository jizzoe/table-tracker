package com.swingtech.app.tabletracker.model;

public class LogEventStatus {
	private EventStatusEnum eventStatus;
	private EventFailureReasonEnum failureReason;
	private String errorMessage;
	
	public EventStatusEnum getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(EventStatusEnum eventStatus) {
		this.eventStatus = eventStatus;
	}
	public EventFailureReasonEnum getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(EventFailureReasonEnum failureReason) {
		this.failureReason = failureReason;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
