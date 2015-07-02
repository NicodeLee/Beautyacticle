package com.nicodelee.beautyarticle.http;

// Static wrapper library around AsyncHttpClient

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.nicodelee.beautyarticle.utils.LogUitl;

public class AsnyClient {

	private static AsyncHttpClient client = null;

	public synchronized static AsyncHttpClient getInstence(){
		if(client ==null){
			client = new AsyncHttpClient();
			client.setTimeout(1000*90);
		}
		return client;
	}

//	public static AsyncHttpClient client = new AsyncHttpClient();{
//		client.setTimeout(90 * 1000);//链接超时时间
//	}

	public static AsyncHttpClient syncHttpClient= new SyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		LogUitl.e("get请求url==" + url + "?" + params);
		getClient().get(url, params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		LogUitl.e("post请求url==" + url + "?" + params);
		getClient().post(url, params, responseHandler);
	}

	public static void setCookieStore(PersistentCookieStore cookieStore) {
		getClient().setCookieStore(cookieStore);
	}

	public static AsyncHttpClient getClient() {
		// Return the synchronous HTTP client when the thread is not prepared
		if (Looper.myLooper() == null)
			return syncHttpClient;
		return getInstence();
	}

}