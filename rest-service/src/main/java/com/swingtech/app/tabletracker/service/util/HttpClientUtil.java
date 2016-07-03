package com.swingtech.app.tabletracker.service.util;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientUtil {
	public static HttpResponse sendPostMessage(String url, String body) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		HttpEntity entity = null;

		try {

			entity = new ByteArrayEntity(url.getBytes("UTF-8"));

			post.setEntity(entity);

			response = client.execute(post);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error trying to send post Http.  To this URL:  " + url + ".  With this body:  " + body);
		}

		return response;
	}
}
