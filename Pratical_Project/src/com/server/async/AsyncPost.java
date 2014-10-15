package com.server.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class AsyncPost extends AsyncTask<Void, Void, String> {

	private String url;
	private OnResponse onResponse;

	private ArrayList<NameValuePair> param;

	public AsyncPost(String url, ArrayList<NameValuePair> param,
			OnResponse onResponse) {
		this.url = url;
		this.onResponse = onResponse;
		this.param = param;
	}

	protected void onPostExecute(String response) {
		onResponse.onResponse(response);
	}

	@Override
	protected String doInBackground(Void... params) {

		try {
			HttpPost httpPost = new HttpPost(url);
			if (param != null) {
				UrlEncodedFormEntity urlEncodedEntity = new UrlEncodedFormEntity(
						param);
				httpPost.setEntity(urlEncodedEntity);
				InputStream is = urlEncodedEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			String response = EntityUtils.toString(httpEntity);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
