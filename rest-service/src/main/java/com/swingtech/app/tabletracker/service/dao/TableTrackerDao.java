package com.swingtech.app.tabletracker.service.dao;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import com.jayway.jsonpath.TypeRef;
import com.swingtech.app.tabletracker.model.TableTrackEvent;
import com.swingtech.app.tabletracker.service.error.TableTrackerExcepton;
import com.swingtech.app.tabletracker.service.util.HttpClientUtil;
import com.swingtech.app.tabletracker.service.util.JsonUtil;

public class TableTrackerDao {
	public static final String TABLE_TRACKER_INDEX_NAME = "st_tabletracker";
	public static final String TABLE_TRACKER_DOCUMENT_TYPE = "table_activity_event";
	
	@Value("${elasticsearch.connection.host}")
	public String elasticSearhUrlHost = "localhost";
	
	@Value("${elasticsearch.connection.port}")
	public int elasticSearchPort = 9200;
	
	public void addEvent(String userName, TableTrackEvent event) throws TableTrackerExcepton {
		String url = "http://" + elasticSearhUrlHost + ":" + elasticSearchPort + "/" + TABLE_TRACKER_INDEX_NAME + "/" + TABLE_TRACKER_DOCUMENT_TYPE + "/" + event.getEventId();
		String body = null;
		HttpResponse response = null;
		String result = null;
		
		body = JsonUtil.marshalObjectToJson(event);
		
		System.out.println("\n\nSending POST Request for addEvent:  ");
		System.out.println("   url:  " + url);
		System.out.println("   request length:  " + body.length());
		System.out.println("   request body:  " + body);
		
		try {
			response = HttpClientUtil.sendPostMessage(url, body);
	        result = EntityUtils.toString(response.getEntity());
		} 
		catch (Exception e) {
			throw new TableTrackerExcepton("Error trying to add the event:  " + event, e);
		}

		System.out.println("\n\nRecieved POST Response for addEvent:  ");
		System.out.println("   Status Code:  " + response.getStatusLine().getStatusCode());
		System.out.println("   response length:  " + response.getEntity().getContentLength());
		System.out.println("   response body:  " + result);
		
		if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
			throw new TableTrackerExcepton("Error trying to add the event:  " + event + ".  The index call to elastic search failed.  Status code:  " + response.getStatusLine().getStatusCode() + ".  URL sent to:  '" + url + "'.  Request Body sent:  '" + body + "'.  Response Body returned:  '" + result + "'");
		}
	}
	
	public void addEvents(String userName, List<TableTrackEvent> events) throws TableTrackerExcepton {
		if (events == null || events.isEmpty()) {
			return;
		}
		
		for (TableTrackEvent event : events) {
			addEvent(userName, event);
		}
	}
		
	public List<TableTrackEvent> getCurrentOpenEvents(String userName) throws TableTrackerExcepton {
		return this.getEventsBySearch(userName, "q=current:true");
	}

	public List<TableTrackEvent> getNonCurrentOpenEvents(String userName) throws TableTrackerExcepton {
		return this.getEventsBySearch(userName, "q=current:false");
	}
	
	public TableTrackEvent getEventById(String userName, String eventId) throws TableTrackerExcepton {
		return null;
	}

	public List<TableTrackEvent> getAllEvents(String userName) throws TableTrackerExcepton {
		return this.getEventsBySearch(userName, null);
	}

	private List<TableTrackEvent> getEventsBySearch(String userName, String searchString) throws TableTrackerExcepton {
		String searchStringInUrl = null;
		String url = null;
		HttpResponse response = null;
		String result = null;
		List<TableTrackEvent> events = null;
		String esEventCountJsonPath = "$.hits.total";
		String esEventResponseJsonPath = "$.hits.hits[*]._source";
		Object parsedResultJsonDocument = null;
		int numOfEventResults = 0;
		
		searchStringInUrl = "_search" + (searchString == null || searchString.isEmpty() ? "" : "?" + searchString);
		
		url = "http://" + elasticSearhUrlHost + ":" + elasticSearchPort + "/" + TABLE_TRACKER_INDEX_NAME + "/" + TABLE_TRACKER_DOCUMENT_TYPE + "/" + searchStringInUrl;

		System.out.println("\n\nSending GET Request for get events:  ");
		System.out.println("   url:  " + url);
		
		try {
			response = HttpClientUtil.sendGetMessage(url);
	        result = EntityUtils.toString(response.getEntity());
		} 
		catch (Exception e) {
			throw new TableTrackerExcepton("Error trying to get list of events from this url:  " + url, e);
		}

		System.out.println("\n\nRecieved GET Response for get events:  ");
		System.out.println("   Status Code:  " + response.getStatusLine().getStatusCode());
		System.out.println("   Response length:  " + response.getEntity().getContentLength());
		System.out.println("   Response body:  " + result);
		
		// first check if we got success http status
		if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
			throw new TableTrackerExcepton("Error trying to get list of events from elastic search:  The index call to elastic search failed.  Status code:  " + response.getStatusLine().getStatusCode() + ".  URL sent to:  '" + url + "'.  Response Body returned:  '" + result + "'");
		}
		
		// parse the json document since we'll be making 2 jsonPath calls and don't want to re-parse everytime.
		parsedResultJsonDocument = JsonUtil.parseJson(result);

		// now validate that we actually got results back
		numOfEventResults = JsonUtil.unmarshalJsonToObjectUsingJsonPath(parsedResultJsonDocument, esEventCountJsonPath, Integer.class);
		
		if (numOfEventResults <= 0) {
			return null;
		}
		
		// Ok, we're good.  Now we can get the events from the result.
        events = JsonUtil.unmarshalJsonToObjectUsingJsonPath(parsedResultJsonDocument, esEventResponseJsonPath, new TypeRef<List<TableTrackEvent>>(){});
        
		return events;
	}
}
