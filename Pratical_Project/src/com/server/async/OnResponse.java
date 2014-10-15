package com.server.async;

public interface OnResponse {
	public void onResponse(String response);

	public void onXmlDownloadCompletion(String newsId, String fileName,
			String threadName);

}
