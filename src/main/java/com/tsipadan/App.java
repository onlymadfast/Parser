package com.tsipadan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class App {

  private static final String URL = "https://gpsfront.aliexpress.com/getRecommendingResults.do";
  private static final int WIDGET_ID = 5547572;
  private static final int COUNT = 100;
  private static final int STEP = 40;

  public static void main(String[] args) throws IOException {

    long start = System.currentTimeMillis();

    String postback = UUID.randomUUID().toString();
    Set<AliModel> products = getProducts(postback);
    writeToFile(setToCsv(products));

    System.out.println((System.currentTimeMillis() - start) + " ms");
  }

  private static String setToCsv(Set<AliModel> set){

    StringBuilder builder = new StringBuilder(AliModel.getFieldsName()).append("\n");
    set.stream().map(AliModel::getFields).forEach(builder::append);

    return builder.toString();
  }

  private static Set<AliModel> getProducts(String postback) throws IOException {

    Set<AliModel> products = new HashSet<>();

    for (int i = 0; products.size() < App.COUNT; i += App.STEP) {
      Parser parser = new Parser(App.STEP, i, App.WIDGET_ID, postback, App.URL);
      JSONObject json = parser.getJson();
      JSONArray results = json.getJSONArray("results");

      for (Object obj : results){
        products.add(new AliModel((JSONObject) obj));
        if (products.size() == App.COUNT) {
          break;
        }
      }
      if (products.size() == App.COUNT) {
        break;
      }
    }
    return products;
  }

  private static void writeToFile(String content) throws FileNotFoundException {

    PrintWriter writer = new PrintWriter(new File("flashResult"));
    writer.write(content);
    writer.flush();
    writer.close();

  }


}




