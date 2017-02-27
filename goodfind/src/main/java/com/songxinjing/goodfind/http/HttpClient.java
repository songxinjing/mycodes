package com.songxinjing.goodfind.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClient {

	private static CloseableHttpClient httpclient = HttpClients.createDefault();

	public static CloseableHttpResponse httpGet(String url) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		return httpclient.execute(httpGet);
	}

	public static CloseableHttpResponse httpPost(String url, List<NameValuePair> nvps)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		return httpclient.execute(httpPost);
	}

}
