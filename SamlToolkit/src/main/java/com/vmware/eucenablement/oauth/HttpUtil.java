package com.vmware.eucenablement.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by chenzhang on 2017-08-07.
 */
public class HttpUtil {

    public static String http(String url) throws IOException {
        System.out.println("HTTP GET "+url);
        HttpURLConnection connection=getUrlConnection(url);
        connection.setRequestMethod("GET");

        InputStream stream=connection.getInputStream();

        int responseCode = connection.getResponseCode();
        System.out.println("Response: "+responseCode);
        if (responseCode!=200)
            throw new IOException("Unable to connect to "+url+": HTTP "+responseCode);

        return readFromStream(stream);
    }

    public static String http(String url, Map<String, ?> params) throws IOException {
        if (params==null) return http(url);
        System.out.println("HTTP POST "+url);
        HttpURLConnection connection=getUrlConnection(url);

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        PrintWriter out=new PrintWriter(connection.getOutputStream());
        out.print(mapToParam(params));
        out.flush();

        InputStream stream=connection.getInputStream();

        int responseCode = connection.getResponseCode();
        System.out.println("Response: "+responseCode);
        if (responseCode!=200)
            throw new IOException("Unable to connect to "+url+": HTTP "+responseCode);

        return readFromStream(stream);
    }

    private static HttpURLConnection getUrlConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        return connection;
    }

    private static String mapToParam(Map<String, ?> map) throws IOException {
        StringBuilder builder=new StringBuilder();
        for (Map.Entry<String, ?> entry: map.entrySet()) {
            if (builder.length()>0) builder.append('&');
            builder.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue().toString(), "utf-8"));
        }
        return builder.toString();
    }

    private static String readFromStream(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuilder sb=new StringBuilder();
        while((line = br.readLine()) != null ){
            sb.append(line);
        }

        return sb.toString();
    }

}
