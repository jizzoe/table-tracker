package com.swingtech.app.tabletracker.service.main;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.swingtech.app.tabletracker.model.TableTrackEvent;
import com.swingtech.app.tabletracker.service.util.DateTimeUtil;
import com.swingtech.app.tabletracker.service.util.JsonUtil;

import net.minidev.json.JSONArray;

public class TestProgram {

	public static void main(String[] args) {
//		printEventJson();
//		jsonPathStuff();
		jacksonTest();
	}
	
	public static void jacksonTest() {
		String json = "[{\"eventId\":\"1467570791951\",\"userName\":\"jizzoe\",\"eventStartTimestamp\":\"2016-07-02T20:04:41.017\",\"eventEndTimestamp\":\"2016-07-02T20:04:51.017\",\"moneyAtStart\":500,\"moneyAtEnd\":100,\"notes\":\"Hey I did it\",\"current\":true},{\"eventId\":\"1467570867962\",\"userName\":\"jizzoe\",\"eventStartTimestamp\":\"2016-07-02T20:04:41.048\",\"eventEndTimestamp\":\"2016-07-02T20:04:41.017\",\"moneyAtStart\":500,\"moneyAtEnd\":100,\"notes\":\"Hey I did it\",\"current\":true}]";

		List<TableTrackEvent> dummy = new ArrayList<TableTrackEvent>();

		List<TableTrackEvent> events = JsonUtil.unmarshalJsonToObject(json, new TypeReference<List<TableTrackEvent>>(){});
		TableTrackEvent event = events.get(0);
		
		Long durationMilis = DateTimeUtil.getTimeDifferenceMilis(event.getEventStartTimestamp(), event.getEventEndTimestamp());
		String durationDisplayString = DateTimeUtil.getTimeDifferenceDisplayString(event.getEventStartTimestamp(), event.getEventEndTimestamp());
		
		System.out.println("  start date:  " + event.getEventStartTimestamp());
		System.out.println("  end date:  " + event.getEventEndTimestamp());
		System.out.println("  end date:  " + durationMilis);
		System.out.println("  end date:  " + durationDisplayString);
		
		System.out.println("event size " + events);
	}
	
	public static void jsonPathStuff() {
//		String esJson = "{\"took\":9,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\":{\"total\":2,\"max_score\":0.30685282,\"hits\":[{\"_index\":\"st_tabletracker\",\"_type\":\"table_activity_event\",\"_id\":\"1467570791951\",\"_score\":0.30685282,\"_source\":{\"eventId\":\"1467570791951\",\"userName\":\"jizzoe\",\"eventStartTimestamp\":\"2016-07-02T20:04:41.048\",\"eventEndTimestamp\":\"2016-07-02T20:04:41.017\",\"moneyAtStart\":500.0,\"moneyAtEnd\":100.0,\"notes\":\"Hey I did it\",\"current\":true}},{\"_index\":\"st_tabletracker\",\"_type\":\"table_activity_event\",\"_id\":\"1467570867962\",\"_score\":0.30685282,\"_source\":{\"eventId\":\"1467570867962\",\"userName\":\"jizzoe\",\"eventStartTimestamp\":\"2016-07-02T20:04:41.048\",\"eventEndTimestamp\":\"2016-07-02T20:04:41.017\",\"moneyAtStart\":500.0,\"moneyAtEnd\":100.0,\"notes\":\"Hey I did it\",\"current\":true}}]}}";
		String esJson = "{\"took\":11,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\":{\"total\":3,\"max_score\":1.0,\"hits\":[{\"_index\":\"st_tabletracker\",\"_type\":\"table_activity_event\",\"_id\":\"1467577235850\",\"_score\":1.0,\"_source\":{\"eventId\":\"1467577235850\",\"userName\":\"jizzoe\",\"eventStartTimestamp\":\"2016-07-02T20:04:41.048\",\"eventEndTimestamp\":\"2016-07-02T20:04:41.017\",\"moneyAtStart\":500.0,\"moneyAtEnd\":100.0,\"notes\":\"Hey I did it\",\"current\":true}},{\"_index\":\"st_tabletracker\",\"_type\":\"table_activity_event\",\"_id\":\"1467570791951\",\"_score\":1.0,\"_source\":{\"eventId\":\"1467570791951\",\"userName\":\"jizzoe\",\"eventStartTimestamp\":\"2016-07-02T20:04:41.048\",\"eventEndTimestamp\":\"2016-07-02T20:04:41.017\",\"moneyAtStart\":500.0,\"moneyAtEnd\":100.0,\"notes\":\"Hey I did it\",\"current\":true}},{\"_index\":\"st_tabletracker\",\"_type\":\"table_activity_event\",\"_id\":\"1467570867962\",\"_score\":1.0,\"_source\":{\"eventId\":\"1467570867962\",\"userName\":\"jizzoe\",\"eventStartTimestamp\":\"2016-07-02T20:04:41.048\",\"eventEndTimestamp\":\"2016-07-02T20:04:41.017\",\"moneyAtStart\":500.0,\"moneyAtEnd\":100.0,\"notes\":\"Hey I did it\",\"current\":true}}]}}";
		String jsonPath = null;
		int numOfHits = 0;
		
		jsonPath = "$.hits.total";
//		jsonPath = "$.hits.hits[0]._source";
//		jsonPath = "$.hits.hits[*]._source";
//		jsonPath = "$.hits.hits[*]._source.eventId";
		
		Object document = JsonUtil.parseJson(esJson);

		Object value = JsonUtil.unmarshalJsonToObjectUsingJsonPath(document, jsonPath); 
		
		System.out.println("value.  type:  " + value.getClass().getName() + ".  Value:  " + value);

		numOfHits = JsonUtil.unmarshalJsonToObjectUsingJsonPath(esJson, jsonPath, Integer.class);

		System.out.println("# of events:  " + numOfHits);
		
		System.out.println("# of events:  " + numOfHits);

		jsonPath = "$.hits.hits[*]._source";
		
		List<TableTrackEvent> events = JsonUtil.unmarshalJsonToObjectUsingJsonPath(document, jsonPath, new TypeRef<List<TableTrackEvent>>(){});
		
		for (TableTrackEvent event : events) {
			int i = 0;
			System.out.println("event # " + ++i);
			System.out.println("  Id:  " + event.getEventId());
			System.out.println("  User Name:  " + event.getUserName());
			System.out.println("  start date:  " + event.getEventStartTimestamp());
		}
		
	}

	public static void printEventJson() {
		// TODO Auto-generated method stub
		TableTrackEvent event = new TableTrackEvent();
		
		event.setEventEndTimestamp(new LocalDateTime());
		event.setEventStartTimestamp(new LocalDateTime());
		event.setMoneyAtEnd(100.00);
		event.setMoneyAtStart(500.00);
		event.setNotes("Hey I did it");
		event.setUserName("jizzoe");
		
		String json = JsonUtil.marshalObjectToJson(event);
		
		System.out.println(json);
		
		String inJson = "{\"userName\":\"jizzoe\",\"eventStartTimestamp\":[2016,7,2,17,46,1,180],\"eventEndTimestamp\":[2016,7,2,17,46,1,127],\"moneyAtStart\":500.0,\"moneyAtEnd\":100.0,\"notes\":\"Hey I did it\"}";
		
		TableTrackEvent outEvent = JsonUtil.unmarshalJsonToObject(json, TableTrackEvent.class);
		
		System.out.print(outEvent);
	}
}
