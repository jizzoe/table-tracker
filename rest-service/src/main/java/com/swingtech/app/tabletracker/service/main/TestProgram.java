package com.swingtech.app.tabletracker.service.main;

import org.joda.time.LocalDateTime;

import com.swingtech.app.tabletracker.model.TableTrackEvent;
import com.swingtech.app.tabletracker.service.util.JsonUtil;

public class TestProgram {

	public static void main(String[] args) {
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
