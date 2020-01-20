package com.rohitshinde.app.moviesapp.web;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebHelper {

    HttpsURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    String response="";

    // function to get json from url
    public JSONObject fetchDataFromServer(String url) throws JSONException {
        JSONObject jsonObject=new JSONObject();
        try {
            URL getUrl = new URL(url);
            httpURLConnection = (HttpsURLConnection) getUrl.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Connection", "close");
            httpURLConnection.setDoInput(true);

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                String inputLine = "";
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                StringBuffer responseString = new StringBuffer();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    responseString.append(inputLine);
                }
                bufferedReader.close();
                bufferedReader = null;
                //get response string
                response = responseString.toString();
                jsonObject=new JSONObject(response);
            }

        } catch (ProtocolException e) {
            jsonObject.put("error","Error in server");
        } catch (IOException e) {
            jsonObject.put("error","Poor internet connection");
        }
        finally{
            httpURLConnection.disconnect();
        }
        // return JSON
        return jsonObject;
    }
}
