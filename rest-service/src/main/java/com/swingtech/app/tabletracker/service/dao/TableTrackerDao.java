package com.swingtech.app.tabletracker.service.dao;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.common.Table;

import com.swingtech.app.tabletracker.model.TableTrackEvent;
import com.swingtech.app.tabletracker.service.error.TableTrackerExcepton;
import com.swingtech.app.tabletracker.service.util.HttpClientUtil;
import com.swingtech.app.tabletracker.service.util.JsonUtil;

public class TableTrackerDao {
	public static final String TABLE_TRACKER_INDEX_NAME = "st_tabletracker";
	public static final String TABLE_TRACKER_DOCUMENT_TYPE = "table_activity_event";
	
	public String elasticSearhUrlHost = "localhost";
	public int elasticSearchPort = 9200;
	
	public void addEvent(TableTrackEvent event) throws TableTrackerExcepton {
		String url = "http://" + elasticSearhUrlHost + ":" + elasticSearchPort + "/" + TABLE_TRACKER_INDEX_NAME + "/" + TABLE_TRACKER_DOCUMENT_TYPE + "/" + event.getEventId();
		String body = null;
		String results = null;
		HttpResponse response = null;
		String result = null;
		
		body = JsonUtil.marshalObjectToJson(event);

		try {
			response = HttpClientUtil.sendPostMessage(url, body);
	        result = EntityUtils.toString(response.getEntity());
		} 
		catch (Exception e) {
			throw new TableTrackerExcepton("Error trying to add the event:  " + event, e);
		}
		
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new TableTrackerExcepton("Error trying to add the event:  " + event + ".  The index call to elastic search failed.  Status code:  " + response.getStatusLine().getStatusCode() + ".  URL sent to:  '" + url + "'.  Request Body sent:  '" + body + "'.  Response Body returned:  '" + result + "'");
		}
	}
	
	public void addEvents(List<TableTrackEvent> events) throws TableTrackerExcepton {
		
	}
		
	public List<TableTrackEvent> getCurrentOpenEvents() throws TableTrackerExcepton {
		return null;
	}
	
	public TableTrackEvent getEventById(String eventId) throws TableTrackerExcepton {
		return null;
	}

	public List<Table> getAllEvents() throws TableTrackerExcepton {
		return null;
	}
}
