package com.swingtech.app.tabletracker.service.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientUtil {
	public static HttpResponse sendPostMessage(String url, String body) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = null;
		HttpResponse response = null;
		HttpEntity entity = null;

		post = new HttpPost(url);
		
		try {

			entity = new ByteArrayEntity(body.getBytes("UTF-8"));

			post.setEntity(entity);

			response = client.execute(post);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error trying to send post Http.  To this URL:  " + url + ".  With this body:  " + body);
		}

		return response;
	}
	
	public static HttpResponse sendGetMessage(String url) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = null;
		HttpResponse response = null;
		HttpEntity entity = null;

		get = new HttpGet(url);
		
		try {
			response = client.execute(get);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error trying to send post get.  To this URL:  " + url);
		}

		return response;
	}
}
