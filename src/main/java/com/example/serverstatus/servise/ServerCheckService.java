package com.example.serverstatus.servise;

import com.example.serverstatus.model.ServerCheck;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ServerCheckService {
    public ServerCheck checkStatus(String url) {
        ServerCheck check = new ServerCheck();
        check.setUrl(url);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(2000);
            connection.connect();
            int code = connection.getResponseCode();
            check.setStatus((code >= 200 && code < 400) ? "available" : "unavailable");
            check.setCode(code);
        } catch (Exception e) {
            check.setStatus("unavailable");
            check.setCode(-1);
        }
        return check;
    }
}