package com.swingtech.app.tabletracker.service.rest;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swingtech.app.tabletracker.model.EventStatusEnum;
import com.swingtech.app.tabletracker.model.LogEventStatus;
import com.swingtech.app.tabletracker.model.TableTrackEvent;
import com.swingtech.app.tabletracker.service.error.TableTrackerExcepton;
import com.swingtech.app.tabletracker.service.service.TableTrackerService;

@RestController
public class TableTrackerController {
	private static final String DUMMY_USER_NAME = "jizzoe";
	TableTrackerService tableTrackerService = new TableTrackerService();
	
    @RequestMapping(value="/table-event-start", method=RequestMethod.POST)
    public ResponseEntity<?>  logEventStart(@RequestBody TableTrackEvent event, @RequestParam(value="overrideCurrentEvent", required = false) Boolean overrideCurrentEvent) {
    	System.out.println("Hey Got an event:  " + event);
    	LogEventStatus logEventStatus = null;
    	ResponseEntity<?> responseEntity = null;
    	
    	if (overrideCurrentEvent == null) {
    		overrideCurrentEvent = false;
    	}
    	
    	System.out.println("\n\nIncoming Add Event Request.  overrideCurrentEvent:  " + overrideCurrentEvent);
    	
    	event.setUserName(DUMMY_USER_NAME);
    	
    	logEventStatus = tableTrackerService.logEventStart(DUMMY_USER_NAME, event, overrideCurrentEvent);
    	
    	if (logEventStatus.getEventStatus().equals(EventStatusEnum.SUCCESS)) {
        	responseEntity = new ResponseEntity<>(logEventStatus, HttpStatus.CREATED);
    	} else {
        	responseEntity = new ResponseEntity<>(logEventStatus, HttpStatus.valueOf(logEventStatus.getFailureReason().getHttpStatusCode()));
    	}

    	return responseEntity;
    }

    @RequestMapping(value="/table-event-end", method=RequestMethod.POST)
    public ResponseEntity<?>  addNewTableEvent(@RequestBody TableTrackEvent event) {
    	System.out.println("Hey Got an event:  " + event);
    	LogEventStatus logEventStatus = null;
    	ResponseEntity<?> responseEntity = null;
    	
    	event.setUserName(DUMMY_USER_NAME);
    	
    	logEventStatus = tableTrackerService.logEventEnd(DUMMY_USER_NAME, event);
    	
    	if (logEventStatus.getEventStatus().equals(EventStatusEnum.SUCCESS)) {
        	responseEntity = new ResponseEntity<>(logEventStatus, HttpStatus.CREATED);
    	} else {
        	responseEntity = new ResponseEntity<>(logEventStatus, HttpStatus.valueOf(logEventStatus.getFailureReason().getHttpStatusCode()));
    	}

    	return responseEntity;
    }

    @RequestMapping(value="/table-events", method=RequestMethod.GET)
    public ResponseEntity<?> getEventTableEvents() {
    	System.out.println("returning list of all events");
    	ResponseEntity<?> responseEntity = null;
    	List<TableTrackEvent> events = null;
    	
		try {
			events = tableTrackerService.getAllEvents(DUMMY_USER_NAME);
		} catch (TableTrackerExcepton e) {
        	responseEntity = getResponseEntityForTableList(null, e);
        	return responseEntity;
		}

		responseEntity = getResponseEntityForTableList(events, null);
    	
    	return responseEntity;
    }

    @RequestMapping(value="/table-events/open", method=RequestMethod.GET)
    public ResponseEntity<?> getCurrentOpenEventTableEvents() {
    	System.out.println("returning list of all events");
    	ResponseEntity<?> responseEntity = null;
    	List<TableTrackEvent> events = null;
    	
		try {
			events = tableTrackerService.getCurrentOpenEvents(DUMMY_USER_NAME);
		} catch (TableTrackerExcepton e) {
        	responseEntity = getResponseEntityForTableList(null, e);
        	return responseEntity;
		}

		responseEntity = getResponseEntityForTableList(events, null);
    	
    	return responseEntity;
    }

    @RequestMapping(value="/table-events/closed", method=RequestMethod.GET)
    public ResponseEntity<?> getNonOpenEventTableEvents() {
    	System.out.println("returning list of all events");
    	ResponseEntity<?> responseEntity = null;
    	List<TableTrackEvent> events = null;
    	
		try {
			events = tableTrackerService.getNonCurrentOpenEvents(DUMMY_USER_NAME);
		} catch (TableTrackerExcepton e) {
        	responseEntity = getResponseEntityForTableList(null, e);
        	return responseEntity;
		}

		responseEntity = getResponseEntityForTableList(events, null);
    	
    	return responseEntity;
    }
    
    private ResponseEntity<?> getResponseEntityForTableList(List<TableTrackEvent> events, Exception e) {
    	ResponseEntity<?> responseEntity = null;
    	
    	if (e != null) {
        	responseEntity = new ResponseEntity<>("{\"status\":\"error\", \"errorMessage\":\"Internal Error occurred:  '" + e.getClass().getName() + ":  " + e.getMessage() + "'\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        	
        	return responseEntity;
    	}

		if (events == null || events.isEmpty()) {
			responseEntity = new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
		} else {
	    	System.out.println("Got list of all table events.  size:  " + events.size());
			responseEntity = new ResponseEntity<>(events, HttpStatus.OK);
		}
    	
		return responseEntity;
    }
    
    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value="/table-event", method=RequestMethod.GET)
    public TableTrackEvent getEventTableEvent() {

		TableTrackEvent event = new TableTrackEvent();

		event.setEventEndTimestamp(new LocalDateTime());
		event.setEventStartTimestamp(new LocalDateTime());
		event.setMoneyAtEnd(100.00);
		event.setMoneyAtStart(500.00);
		event.setNotes("Hey I did it");
		event.setUserName("jizzoe");

    	System.out.println("about to send:  " + event);
    	
    	return event;
    }
}
