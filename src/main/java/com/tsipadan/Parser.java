package com.tsipadan;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Parser {

  private final String path;
  private String response;

  public Parser(int limit, int offset, int widgetId, String postback, String path) {
    this.path = path + "?" + "limit=" + limit + "&offset=" +
        offset + "&widget_id=" + widgetId + "&postback=" + postback;
  }

  public JSONObject getJson() throws IOException {
    response = read();
    return new JSONObject(response);
  }

  private String read() throws IOException {

    URL url = new URL(path);
    URLConnection connection = url.openConnection();
    connection.connect();

    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder builder = new StringBuilder();
    reader.lines().forEach(builder::append);
    reader.close();

    return builder.toString();
  }

  public String getResponse() {
    return response;
  }

}
