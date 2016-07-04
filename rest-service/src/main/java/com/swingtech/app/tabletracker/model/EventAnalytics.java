package com.swingtech.app.tabletracker.model;

public class EventAnalytics {
	private Long timeAtTableMilis;
	private String timeAtTableDisplay;
	private boolean isMoneyUp;
	private Double moneyDifference;
	
	public Long getTimeAtTableMilis() {
		return timeAtTableMilis;
	}
	public void setTimeAtTableMilis(Long timeAtTable) {
		this.timeAtTableMilis = timeAtTable;
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
	public String getTimeAtTableDisplay() {
		return timeAtTableDisplay;
	}
	public void setTimeAtTableDisplay(String timeAtTableDisplay) {
		this.timeAtTableDisplay = timeAtTableDisplay;
	}
}
