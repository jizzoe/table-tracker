package com.swingtech.app.tabletracker.service.rest;

import org.joda.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.swingtech.app.tabletracker.model.TableTrackEvent;

@RestController
public class TableTrackerController {
	
    @RequestMapping(value="/table-event", method=RequestMethod.POST)
    public ResponseEntity<?>  addNewTableEvent(@RequestBody TableTrackEvent event) {
    	System.out.println("Hey Got an event:  " + event);
    	HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand("{\"status\":\"success\"").toUri());
    	return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
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
