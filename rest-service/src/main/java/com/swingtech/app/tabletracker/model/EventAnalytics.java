package com.swingtech.app.tabletracker.model;

import org.joda.time.Duration;

public class EventAnalytics {
	private Duration timeAtTable;
	private boolean isMoneyUp;
	private Double moneyDifference;
	
	public Duration getTimeAtTable() {
		return timeAtTable;
	}
	public void setTimeAtTable(Duration timeAtTable) {
		this.timeAtTable = timeAtTable;
	}
	public boolean isMoneyUp() {
		return isMoneyUp;
	}
	public void setMoneyUp(boolean isMoneyUp) {
		this.isMoneyUp = isMoneyUp;
	}
	public Double getMoneyDifference() {
		return moneyDifference;
	}
	public void setMoneyDifference(Double moneyDifference) {
		this.moneyDifference = moneyDifference;
	}
}
