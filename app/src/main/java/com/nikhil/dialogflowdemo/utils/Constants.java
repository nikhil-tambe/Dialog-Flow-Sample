package com.nikhil.dialogflowdemo.utils;

/**
 * Created by nikhil on 7/1/18.
 */

public interface Constants {

    // Put Your DialogFlow
    // String API_KEY = "";

    // URL Params
    String BASE_URL = "https://api.api.ai/v1/query?v=20150910";
    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer";
    String CONTENT_TYPE = "Content-Type";
    String APPLICATION_JSON = "application/json";
    String VALUE_SESSION_ID = "1234567890";
    String VALUE_LANG_EN = "en";

    // Request JSON keys
    String J_KEY_QUERY = "query";
    String J_KEY_LANG = "lang";
    String J_KEY_SESSION_ID = "sessionId";

    // Response JSON keys
    String J_KEY_RESULT = "result";
    String K_KEY_FULFILLMENT = "fulfillment";
    String J_KEY_SPEECH = "speech";

}
