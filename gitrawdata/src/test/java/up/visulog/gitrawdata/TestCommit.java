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

public class TestCommit {
    @Test
    public void testParseCommit() throws IOException, URISyntaxException {
        var expected = "Commit{id='6304c1acdc1cbdeb8315528781896abc72a021b8', date='Tuesday the 1st of September 2020 at 12h 30min 53sec', author='Aldric Degorre <adegorre@irif.fr>', description='More gradle configuration (with subprojects)'}";
        var uri = getClass().getClassLoader().getResource("git.log").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var commit = Commit.parseCommit(reader);
            assertTrue(commit.isPresent());
            assertEquals(expected, commit.get().toString());
        }
    }

    @Test
    public void testParseLog() throws IOException, URISyntaxException {
        var expected = "[Commit{id='6304c1acdc1cbdeb8315528781896abc72a021b8', date='Tue Sep 1 12:30:53 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='More gradle configuration (with subprojects)'}, Commit{id='c0cf37d6b32897677e4b8f04be012e5379a7ab80', date='Thu Aug 27 23:49:03 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='first setup of project modules and gradle configuration'}, Commit{id='9e74f1581f23aaad21e2b936091d3ce371336e22', date='Mon Aug 31 11:28:28 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md - more modules'}, Commit{id='7484b0cb7b4e69e09c82ed38549750fa2a77f50c', date='Thu Aug 27 00:35:19 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md - translation...'}, Commit{id='9aaf6e09cc30909b32c68b4d5bf4ac50f95ecb93', date='Thu Aug 27 00:33:46 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md - some title left untranslated'}, Commit{id='969e2247156f27f27fec57b13faf6097bf4e2757', date='Thu Aug 27 00:32:47 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md -> in English, with some more details'}, Commit{id='486d76dbfd24ac65eeeeb16e57ae4fd68c8ecb1c', date='Thu Aug 27 00:02:55 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Ajout de README.md avec définition des grandes lignes du sujet.'}]";
        var expectedUTF8 = new String(expected.getBytes(), StandardCharsets.UTF_8);
        var uri = getClass().getClassLoader().getResource("git.log").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var log = Commit.parseLog(reader);
            System.out.println(log);
            assertEquals(expectedUTF8, log.toString());
        }
    }

    @Test
    public void testConversionCalendar()
    {
        String date = "Wed Sep 29 20:33:07 2021 +0200";
        String[] parts = date.split(" ");
        int i = 0;
        for(i = 0; i < parts.length; i++)
        {
            System.out.println(parts[i]);

        }
        String dayString = parts[0];
        String month = parts[1];
        String day = parts[2];
        String year = parts[4];
        String[] time = parts[3].split(":");
        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        System.out.println(hour + "h "+ min+"m "+sec+"s" );
        StringBuilder sb = new StringBuilder();
        sb.append(Day.replaceFr(dayString));
        sb.append(" le");
        sb.append(" "+day);
        sb.append(" "+Month.replaceFr(month));
        sb.append(" "+year);



    }

    static String buildDateEnglish(String date)
    {
        // This is an example of date "Wed Sep 29 20:33:07 2021 +0200"
        //and this is an exemple of what we want to get "Saturday the 13th of April, 2019 at 20h 33min 07sec"
        String[] parts = date.split(" ");

        String dayString = parts[0];
        String month = parts[1];
        String day = parts[2];
        String year = parts[4];
        String[] time = parts[3].split(":");
        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        System.out.println(hour + "h "+ min+"m "+sec+"s" );
        StringBuilder sb = new StringBuilder();
        sb.append(Day.replaceEn(dayString));
        sb.append(" the");
        sb.append(" "+Day.addAfterDay(day));
        sb.append(" of");
        sb.append(" "+Month.replaceEn(month));
        sb.append(" "+year);
        sb.append(" at");
        sb.append(" "+hour+"h");
        sb.append(" "+min+"min");
        sb.append(" "+sec+"sec");
        
        return sb.toString();

    }

    

    @Test
    public void testJourMois()
    {
        String date  = "Thu Feb 23 20:33:07 1998 +0200";
        // String day = "Wed";
        // String month = "Nov";
        // String dayString = "23";
        // System.out.println(day + " est "+Day.replaceEn(day));
        // System.out.println(month +" est "+Month.replaceEn(month));
        // System.out.println(dayString +" est "+Day.addAfterDay(dayString));
        System.out.println(buildDateEnglish(date));

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");	
	    // Calendar calendar1 = new GregorianCalendar(2013,00,28,13,24,56);
        Calendar calendar2 = new GregorianCalendar(2013,01,27,13,24,56);

        // System.out.println(calendar1.compareTo(calendar2));
        // System.out.println(calendar2.compareTo(calendar1));
        System.out.println(calendar2.getTime());


        

    }

    @Test
    public void testCalendar()
    {
        String date  = "Thu Feb 23 20:33:07 1998 +0200";
        String[] parts = date.split(" ");

        String month = parts[1];
        String day = parts[2];
        String year = parts[4];
        String[] time = parts[3].split(":");
        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        Calendar calendar = new GregorianCalendar(Integer.parseInt(year),Month.convertMonth(month),
        Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(min),Integer.parseInt(sec));
        System.out.println(calendar.getTime());

    }


    @Test
    public void testReadJson() throws JSONException, IOException
    {
        JSONObject json = readJsonFromUrl("https://api.github.com/repos/torvalds/linux");
        
        System.out.println(json.toString());
        // System.out.println(json.get("commit"));

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }
    
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

    public static JSONArray readJsonCommitsFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JSONArray json = new JSONArray(jsonText);
        return json;
      } finally {
          is.close();
      }
    }

    
    @Test
    public void testReadJsonCommit() throws JSONException, IOException
    {
      int count=0;
      JSONArray jsonarray = readJsonCommitsFromUrl("https://api.github.com/repos/torvalds/linux/commits?page=1&per_page=1000");
      for(int i=0; i<jsonarray.length();i++){
        System.out.println(jsonarray.getJSONObject(i).getJSONObject("commit").getJSONObject("author").get("name"));
        // System.out.println(jsonarray.getJSONObject(0).get("node_id"));
        // System.out.println(jsonarray.getJSONObject(0).getJSONObject("commit").get("message"));
        // System.out.println(jsonarray.getJSONObject(i).getJSONObject("commit").getJSONObject("author").get("date"));
        count++;
      }System.out.println(count);
    }

    @Test
    public void testToutBete()
    {
      String s = "rrr";
      String sm = "rrr";
      String r = "ssss";
      System.out.println(s == r);
    }


    @Test
    public void testReadJsonAllCommit() throws JSONException, IOException{
      int page=1;
      int count=0;
      int last=1;
      while(last<=1){
        JSONArray jsonarray = readJsonCommitsFromUrl("https://api.github.com/repos/torvalds/linux/commits?page="+page+"&per_page=100");
        for(int i=0; i<jsonarray.length();i++){
          // System.out.println(jsonarray.getJSONObject(i).getJSONObject("commit").getJSONObject("author").get("name"));
          // System.out.println(jsonarray.getJSONObject(0).get("node_id"));
          //System.out.println("- "+jsonarray.getJSONObject(0).getJSONObject("commit").get("message"));
          //System.out.println();
          System.out.println(jsonarray.getJSONObject(i).getJSONObject("commit").getJSONObject("author").get("date"));
          count++;
        }last++;
        page++;
      }System.out.println(count);
    }
    
    @Test
    public void ConvertDatetoNormal() throws ParseException{
      String date = "2019-07-14T18:30:00.000Z";
      SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
      Date parsedDate = inputFormat.parse(date);
      String formattedDate = outputFormat.format(parsedDate);
      System.out.println(formattedDate);
    }


    @Test
    public void testReadJsonAllIssues() throws JSONException, IOException{
      int page=1;
      int count=0;
      int last=0;
      while(last<=0){
        JSONArray jsonarray = readJsonCommitsFromUrl("https://api.github.com/repos/facebook/react/issues?page="+page+"&per_page=100");
        for(int i=0; i<jsonarray.length();i++){
          System.out.println("----------------------------------");
          System.out.println("- "+jsonarray.getJSONObject(i).get("title"));
          System.out.println("number: "+(jsonarray.getJSONObject(i).get("number")));
          System.out.println("date de création: "+(jsonarray.getJSONObject(i).get("created_at")));
          System.out.println("Etat: "+jsonarray.getJSONObject(i).get("state"));

          if(!jsonarray.getJSONObject(i).get("assignee").equals("null")){
            System.out.print("Utilisateur(s) assigné(s): ");
            JSONArray assignees = jsonarray.getJSONObject(i).getJSONArray("assignees");
            for(int j=0;j<assignees.length();j++){
              System.out.print(assignees.getJSONObject(j).get("login")+" ");
            }
          }

          System.out.println();
          System.out.println(jsonarray.getJSONObject(i).get("body"));
          System.out.println("----------------------------------");
          count++;
        }last++;
        page++;
      }System.out.println(count);
    }

    @Test
    public void testToGitLogDate(){
      System.out.println(GithubCommit.toGitLogDate("2001-07-06T18:30:00.000Z"));
      //System.out.println(Calendar.WEDNESDAY);
    }
}

