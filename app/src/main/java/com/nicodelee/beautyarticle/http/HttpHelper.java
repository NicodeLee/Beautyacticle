package com.nicodelee.beautyarticle.http;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Nicodelee on 15/6/15.
 */
public class HttpHelper {
    private RequestParams params;
    private String url;
    private AsyncHttpResponseHandler responseHandler;

    public static class Builder {

        private String url = "";

        private RequestParams params = null;
        private AsyncHttpResponseHandler responseHandler = null;


        public Builder(){
        }


        public Builder toUrl(String burl){
            url = burl;
            return this;
        }

        public Builder addParams(String key, String value) {
            if (params == null) {
                params = new RequestParams();
            }
            params.put(key,value);
            return this;
        }

        public Builder executeGet( AsyncHttpResponseHandler responseHandler){
            AsnyClient.get(url, params, responseHandler);
            return this;
        }

        public Builder executePost( AsyncHttpResponseHandler responseHandler){
            AsnyClient.post(url, params, responseHandler);
            return this;
        }

        public HttpHelper build() {
            return new HttpHelper(this);
        }
    }

    private HttpHelper(Builder builder) {
        params = builder.params;
        url = builder.url;
        responseHandler = builder.responseHandler;
    }

}
