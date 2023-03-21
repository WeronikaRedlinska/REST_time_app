package com.example.trackingapp;

import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;


public class JavaRestClient {


    public static JSONArray get_stats() { //get stats from server

        try {

			
            JSONObject data = new JSONObject();
			data.put("id", macAddress.getIPAddress(true));
            URL url = new URL("http://projektrozproszone.pythonanywhere.com/jsosendstats");
            HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            try(OutputStream os = httpConnection.getOutputStream()) {
                byte[] input = data.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            String info = httpConnection.getResponseMessage();
            Integer responseCode = httpConnection.getResponseCode();
				String inline = "";
				Scanner scanner = new Scanner(httpConnection.getInputStream());
  
				while (scanner.hasNext()) {
					inline += scanner.nextLine();
				}
            scanner.close();

            JSONArray json = new JSONArray(inline);

            return json;


        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
        return null;
    }
	public static void sent_data(String app_name,int t) { //sent data to server

        try {

            JSONObject data = new JSONObject();
			data.put("id", macAddress.getIPAddress(true));
            data.put("nazwa_aplikacji", app_name);
			data.put("czas", t);

            URL url = new URL("https://projektrozproszone.pythonanywhere.com/jsongetdata");
            HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();

            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");

            try(OutputStream os = httpConnection.getOutputStream()) {
                byte[] input = data.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            httpConnection.getResponseMessage();
            httpConnection.disconnect();
        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
        
    
}
