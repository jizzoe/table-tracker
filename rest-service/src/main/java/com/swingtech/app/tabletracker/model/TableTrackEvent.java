package com.swingtech.app.tabletracker.model;

import org.joda.time.LocalDateTime;

public class TableTrackEvent {
	private int eventId;
	private String userName;
	private LocalDateTime eventStartTimestamp;
	private LocalDateTime eventEndTimestamp;
	private Double moneyAtStart;
	private Double moneyAtEnd;
	private String notes;
	private boolean isCurrent;
	private String tableName;
	private String tableType;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public LocalDateTime getEventStartTimestamp() {
		return eventStartTimestamp;
	}
	public void setEventStartTimestamp(LocalDateTime eventStartTimestamp) {
		this.eventStartTimestamp = eventStartTimestamp;
	}
	public LocalDateTime getEventEndTimestamp() {
		return eventEndTimestamp;
	}
	public void setEventEndTimestamp(LocalDateTime eventEndTimestamp) {
		this.eventEndTimestamp = eventEndTimestamp;
	}
	public Double getMoneyAtStart() {
		return moneyAtStart;
	}
	public void setMoneyAtStart(Double moneyAtStart) {
		this.moneyAtStart = moneyAtStart;
	}
	public Double getMoneyAtEnd() {
		return moneyAtEnd;
	}
	public void setMoneyAtEnd(Double moneyAtEnd) {
		this.moneyAtEnd = moneyAtEnd;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Override
	public String toString() {
		return "TableTrackEvent [userName=" + userName + ", eventStartTimestamp=" + eventStartTimestamp
				+ ", eventEndTimestamp=" + eventEndTimestamp + ", moneyAtStart=" + moneyAtStart + ", moneyAtEnd="
				+ moneyAtEnd + ", notes=" + notes + "]";
	}
	public boolean isCurrent() {
		return isCurrent;
	}
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
}