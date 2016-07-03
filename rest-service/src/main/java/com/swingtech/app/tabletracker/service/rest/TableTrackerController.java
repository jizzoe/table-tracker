package com.swingtech.app.tabletracker.service.rest;

import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.swingtech.app.tabletracker.model.LogEventStatus;
import com.swingtech.app.tabletracker.model.TableTrackEvent;
import com.swingtech.app.tabletracker.service.service.TableTrackerService;

@RestController
public class TableTrackerController {
	TableTrackerService tableTrackerService = new TableTrackerService();
	
    @RequestMapping(value="/table-event-start", method=RequestMethod.POST)
    public ResponseEntity<?>  logEventStart(@RequestBody TableTrackEvent event) {
    	System.out.println("Hey Got an event:  " + event);
    	LogEventStatus logEventStatus = null;
    	
    	logEventStatus = tableTrackerService.logEventStart(event, false);

    	return new ResponseEntity<>(logEventStatus, HttpStatus.CREATED);
    }

    @RequestMapping(value="/table-event-end", method=RequestMethod.POST)
    public ResponseEntity<?>  addNewTableEvent(@RequestBody TableTrackEvent event) {
    	System.out.println("Hey Got an event:  " + event);
    	LogEventStatus logEventStatus = null;
    	
    	logEventStatus = tableTrackerService.logEventEnd(event);

    	return new ResponseEntity<>(logEventStatus, HttpStatus.CREATED);
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
