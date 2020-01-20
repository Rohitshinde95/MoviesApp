package com.rohitshinde.app.moviesapp.web;

import android.content.Context;

import com.rohitshinde.app.moviesapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class WebHelper {

    private HttpsURLConnection httpURLConnection;

    // function to get json from url
    public JSONObject fetchDataFromServer(Context context, String url) throws JSONException {
        JSONObject jsonObject = new JSONObject();
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
                String inputLine;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder responseString = new StringBuilder();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    responseString.append(inputLine);
                }
                bufferedReader.close();
                //get response string
                String response = responseString.toString();
                jsonObject = new JSONObject(response);
            }

        } catch (ProtocolException e) {
            jsonObject.put(context.getResources().getString(R.string.key_error), context.getResources().getString(R.string.server_error_message));
        } catch (IOException e) {
            jsonObject.put(context.getResources().getString(R.string.key_error), context.getResources().getString(R.string.no_internet_message));
        } finally {
            httpURLConnection.disconnect();
        }
        // return JSON
        return jsonObject;
    }
}
