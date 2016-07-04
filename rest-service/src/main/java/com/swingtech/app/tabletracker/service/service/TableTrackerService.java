package com.swingtech.app.tabletracker.service.service;

import java.util.List;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swingtech.app.tabletracker.model.EventAnalytics;
import com.swingtech.app.tabletracker.model.EventFailureReasonEnum;
import com.swingtech.app.tabletracker.model.EventStatusEnum;
import com.swingtech.app.tabletracker.model.LogEventStatus;
import com.swingtech.app.tabletracker.model.TableTrackEvent;
import com.swingtech.app.tabletracker.service.dao.TableTrackerDao;
import com.swingtech.app.tabletracker.service.error.TableTrackerExcepton;
import com.swingtech.app.tabletracker.service.util.DateTimeUtil;
import com.swingtech.app.tabletracker.service.util.IdGenertor;

public class TableTrackerService {
	public final static Logger logger = LoggerFactory.getLogger(TableTrackerService.class);;
	
	public TableTrackerDao tableTrackerDao = new TableTrackerDao();  //TableTrackerDao tableTrackerDao;
	
	public LogEventStatus logEventStart(String userName, TableTrackEvent incommingEvent, boolean overrideCurrentEvent) {
		LogEventStatus logEventStatus = new LogEventStatus();
		LogEventStatus testLogEventStatus = null;
		String eventId = null;
		TableTrackEvent existingCurrentEvent = null;
		
		testLogEventStatus = this.validateIncomingStartEvent(incommingEvent, logEventStatus);
		
		if (testLogEventStatus != null) {
			return testLogEventStatus;
		}
		
		try {
			existingCurrentEvent = this.getCurrentOpenEvent(userName);
		} catch (TableTrackerExcepton e) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.SYSTEM_ERROR, "Error trying to log event start.  Could not get the current event", e);
		}
		
		if (existingCurrentEvent != null) {
			if (!overrideCurrentEvent) {
				return this.handleError(logEventStatus, EventFailureReasonEnum.CURRENT_EVENT_EXISTS_FOR_START_LOGGING, "Trying to log a new event, but there is already a current one:  " + existingCurrentEvent);
			}
			
			eventId = existingCurrentEvent.getEventId();
		} else {
			eventId = IdGenertor.generateNewId();
		}
		
		incommingEvent = this.addEventStartInfo(eventId, incommingEvent);
		
		try {
			tableTrackerDao.addEvent(userName, incommingEvent);
		} catch (TableTrackerExcepton e) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.SYSTEM_ERROR, "Error trying to log event start.  Error adding event to data store.", e);
		}

		logEventStatus.setEventStatus(EventStatusEnum.SUCCESS);
		
		return logEventStatus;
	}

	public LogEventStatus logEventEnd(String userName, TableTrackEvent incommingEvent) {
		LogEventStatus logEventStatus = new LogEventStatus();
		LogEventStatus testLogEventStatus = null;
		TableTrackEvent existingCurrentEvent = null;

		try {
			existingCurrentEvent = this.getCurrentOpenEvent(userName);
		} catch (TableTrackerExcepton e) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.SYSTEM_ERROR, "Error trying to log event end", e);
		}
		
		if (existingCurrentEvent == null) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.NO_CURRENT_EVENT_FOR_END_LOGGING, "There is no current event to log the end of");
		}

		testLogEventStatus = this.validateIncomingEndEvent(incommingEvent, existingCurrentEvent, logEventStatus);
		
		if (testLogEventStatus != null) {
			return testLogEventStatus;
		}
		
		existingCurrentEvent = this.addEventEndInfo(incommingEvent, existingCurrentEvent);

		try {
			tableTrackerDao.addEvent(userName, existingCurrentEvent);
		} catch (TableTrackerExcepton e) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.SYSTEM_ERROR, "Error trying to log event end.", e);
		}
		
		logEventStatus.setEventStatus(EventStatusEnum.SUCCESS);
		
		return logEventStatus;
	}
	
	private LogEventStatus validateIncomingStartEvent(TableTrackEvent incommingStartEvent, LogEventStatus logEventStatus) {
		if (incommingStartEvent.getUserName() == null || incommingStartEvent.getMoneyAtStart() == null) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.INVALID_START_EVENT_REQUEST, "The start event is invalid.  Start event must contain user name and starting money.  incommingStartEvent values:  " + incommingStartEvent);
		}
		
		return null;
	}

	private LogEventStatus validateIncomingEndEvent(TableTrackEvent incommingStartEvent, TableTrackEvent existingCurrentEvent, LogEventStatus logEventStatus) {
		if (incommingStartEvent.getMoneyAtEnd() == null) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.INVALID_END_EVENT_REQUEST, "The end event is invalid.  End event must contain end money.  incommingStartEvent values:  " + incommingStartEvent);
		}

		if (existingCurrentEvent.getEventId() == null || existingCurrentEvent.getEventStartTimestamp() == null || existingCurrentEvent.getMoneyAtStart() == null || existingCurrentEvent.getUserName() == null) {
			return this.handleError(logEventStatus, EventFailureReasonEnum.INVALID_CURRENT_EVENT_FOR_END_EVENT_REQUEST, "The current is invalid.  Can not log end event.  Current event must contain event id, user name, starting money, and starting timestamp.  incommingStartEvent values:  " + incommingStartEvent);
		}
		
		return null;
	}

	private TableTrackEvent addEventStartInfo(String eventId, TableTrackEvent incommingEvent) {
		incommingEvent.setEventId(eventId);
		incommingEvent.setCurrent(true);
		
		if (incommingEvent.getEventStartTimestamp() == null) {
			incommingEvent.setEventStartTimestamp(new LocalDateTime());
		}
		
		return incommingEvent;
	}

	private TableTrackEvent addEventEndInfo(TableTrackEvent incommingEvent, TableTrackEvent existingCurrentEvent) {
		EventAnalytics eventAnalytics;

		if (incommingEvent.getEventStartTimestamp() != null) {
			existingCurrentEvent.setEventEndTimestamp(incommingEvent.getEventStartTimestamp());
		} else {
			existingCurrentEvent.setEventEndTimestamp(new LocalDateTime());
		}
		
		existingCurrentEvent.setCurrent(false);
		existingCurrentEvent.setMoneyAtEnd(incommingEvent.getMoneyAtEnd());
		
		eventAnalytics = this.createEventAnalytics(existingCurrentEvent);
		
		existingCurrentEvent.setEventAnalytics(eventAnalytics);
		
		return existingCurrentEvent;
	}
	
	private EventAnalytics createEventAnalytics(TableTrackEvent existingCurrentEvent) {
		EventAnalytics eventAnalytics = new EventAnalytics();
		Double moneyDifference = 0.0;
		Duration duration = null;
		
		moneyDifference = existingCurrentEvent.getMoneyAtEnd() - existingCurrentEvent.getMoneyAtStart();
		
		eventAnalytics.setMoneyDifference(moneyDifference);
		eventAnalytics.setMoneyUp(moneyDifference > 0);
		eventAnalytics.setTimeAtTableMilis(DateTimeUtil.getTimeDifferenceMilis(existingCurrentEvent.getEventStartTimestamp(), existingCurrentEvent.getEventEndTimestamp()));
		eventAnalytics.setTimeAtTableDisplay(DateTimeUtil.getTimeDifferenceDisplayString(existingCurrentEvent.getEventStartTimestamp(), existingCurrentEvent.getEventEndTimestamp()));
		
		return eventAnalytics;
	}
	
	private LogEventStatus handleError(LogEventStatus logEventStatus, EventFailureReasonEnum failureReason, String errorMessage, Exception e) {
		logEventStatus.setEventStatus(EventStatusEnum.FAILURE);
		logEventStatus.setFailureReason(failureReason);
		logEventStatus.setErrorMessage(errorMessage);
		String logMessage = null;
		
		if (e != null) {
			logEventStatus.setErrorMessage(logEventStatus.getErrorMessage() + ".  Exception:  " + e.getClass().getName() + ":  " + e.getMessage());
			logMessage = "Error encounterd.  Log Status Details:  " + logEventStatus;
			logger.error(logMessage, e);
		} else {
			logMessage = "Error encounterd.  Log Status Details:  " + logEventStatus;
			logger.error(logMessage);
		}
		
		return logEventStatus;
	}

	private LogEventStatus handleError(LogEventStatus logEventStatus, EventFailureReasonEnum failureReason, String errorMessage) {
		return this.handleError(logEventStatus, failureReason, errorMessage, null);
	}
	
	private LogEventStatus handlError(LogEventStatus logEventStatus, EventFailureReasonEnum failureReason) {
		return this.handleError(logEventStatus, failureReason, null);
	}

	public List<TableTrackEvent> getCurrentOpenEvents(String userName) throws TableTrackerExcepton {
		List<TableTrackEvent> currentOpenEventList = null;
		
		currentOpenEventList = tableTrackerDao.getCurrentOpenEvents(userName);
		
		if (currentOpenEventList == null || currentOpenEventList.isEmpty()) {
			return null;
		}
		
		return currentOpenEventList;
	}

	public List<TableTrackEvent> getNonCurrentOpenEvents(String userName) throws TableTrackerExcepton {
		List<TableTrackEvent> currentOpenEventList = null;
		
		currentOpenEventList = tableTrackerDao.getNonCurrentOpenEvents(userName);
		
		if (currentOpenEventList == null || currentOpenEventList.isEmpty()) {
			return null;
		}
		
		return currentOpenEventList;
	}
	
	public TableTrackEvent getCurrentOpenEvent(String userName) throws TableTrackerExcepton {
		List<TableTrackEvent> currentOpenEventList = null;
		TableTrackEvent currentOpenEvent = null;
		
		currentOpenEventList = tableTrackerDao.getCurrentOpenEvents(userName);
		
		if (currentOpenEventList == null || currentOpenEventList.isEmpty()) {
			return null;
		}
		
		if (currentOpenEventList.size() == 1) {
			currentOpenEvent = currentOpenEventList.get(0);
		} else {
			// if there's more than 1 event, then:
			//   *  set currentOpenEvent to the most recent event.
			//   *  Set all the others to isCurrent = false
			//   *  Save all the ones that changed
			
			//currentOpenEvent = 
		}
		
		return currentOpenEvent;
	}
	
	public TableTrackEvent getEventById(String userName, String eventId) {
		return null;
	}
	
	public List<TableTrackEvent> getAllEvents(String userName) throws TableTrackerExcepton {
		return tableTrackerDao.getAllEvents(userName);
	}
}
