package eu.alfred.personalization_manager.db_administrator.api.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Custom Request for Volley API, as StringRequest acts as if it was function
 * String function(String param)
 * and JSONRequest acts like
 * JSON function(JSON param)
 * and what we need is a mixture of both
 * String function(JSON param)
 */
class UPRequest extends Request<String> {
    private final String REQUEST_TYPE_APP_JSON = "application/json; charset=utf-8";


    private JSONObject mBody;
        private final Response.Listener<String> mListener;

        /**
         *
         * @param method Usually Request.Method.POST or Request.Method.PUT
         * @param url complete URL (String) like "new" or "54e5cd60..."
         * @param body JSON Object we want to persist.
         * @param listener If success, what we do after the call.
         * @param errorListener If error, what we do after the call.
         */
        UPRequest(int method, String url, JSONObject body, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            mBody = body;
            mListener = listener;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            return mBody.toString().getBytes();

        }

        @Override
        public String getBodyContentType() {
            return REQUEST_TYPE_APP_JSON;
        }

        /**
         * This is where we parse the response, in our case a simple String with an ID
         * @param response ID of the object we have persisted.
         * @return parsed response.
         */
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                String id = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(id, HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }
    }