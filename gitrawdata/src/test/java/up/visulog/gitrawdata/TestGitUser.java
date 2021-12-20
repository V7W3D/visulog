package up.visulog.gitrawdata;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

//import org.json.JSONException;
//import org.json.JSONObject;
import org.json.*;

public class TestGitUser {

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }

    @Test
    public void testGetUser() throws JSONException, IOException
    {
      try{
        JSONObject json = readJsonFromUrl("https://api.github.com/users/torvalds");

      }catch (Exception e){
        System.out.println(e);
        return;

      }
      JSONObject json = readJsonFromUrl("https://api.github.com/users/torvalds");
      int  id = (Integer)json.get("id");
      String type = (String)json.get("type");
      boolean site_admin = (boolean)json.get("site_admin");
      String company = json.get("company") == null ? "" : (String)json.get("company");
      String name = json.get("name") == null ? "" : (String)json.get("name");
      String location = json.get("location") == null ? "" : (String)json.get("location");
      String email = json.get("email") == null ? "" : (String)json.get("email");
      int followers;
      int following;
      String hireable;
      String createdAt;
      String bio;
      String twitterUserName;
      System.out.println(json.get("location"));

    }

    
}
