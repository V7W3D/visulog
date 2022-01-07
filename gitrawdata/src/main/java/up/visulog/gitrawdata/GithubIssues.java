package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.*;

public class GithubIssues implements Parsable {
  
  public String title;
  public int number;
  public String creationDate;
  public String state;
  public List<String> assignedUsers;
  
  private static List<Parsable> listIssues;
    // Limite les call de REST API
  public static final int limit=2;

  public GithubIssues(String title, int number, String creationDate, String state, List<String> assignedUsers) {
    this.title = title;
    this.number = number;
    this.creationDate = buildDateEnglish(creationDate);
    this.state = state;
    this.assignedUsers = assignedUsers;
  }

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONArray readJsonIssuesFromUrl(String url) throws IOException, JSONException {
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

  public static List<Parsable> getGithubIssues(String url){
    try{
        listIssues=getIssuesFromURL(url);
    }catch(Exception e){
      System.out.println(e);
    }
    return listIssues;
  }

  public static List<Parsable> getIssuesFromURL(String pValue) throws JSONException, IOException{
    List<Parsable> issues= new ArrayList<Parsable>();
    try{
      JSONArray jsonarray = readJsonIssuesFromUrl("https://api.github.com/repos/"+pValue+"/issues");
    }catch (Exception e){
      System.out.println("Probleme: "+e);
      return issues;
    }int page=1;
    int count=0;
    while(count<limit){
      JSONArray jsonarray = readJsonIssuesFromUrl("https://api.github.com/repos/"+pValue+"/issues?page="+page+"&per_page=100");  
      if(!jsonarray.isEmpty()){
        for(int i=0;i<jsonarray.length();i++){
          GithubIssueBuilder builder=new GithubIssueBuilder();
          String title=(String) jsonarray.getJSONObject(i).get("title");
          builder.setTitle(title);
          int number=(int) jsonarray.getJSONObject(i).get("number");
          builder.setNumber(number);
          String creationDate=GithubCommit.toGitLogDate((String) jsonarray.getJSONObject(i).get("created_at"));
          builder.setCreationDate(creationDate);
          String state=(String) jsonarray.getJSONObject(i).get("state");
          builder.setState(state);
          List<String> assignedUsers=new LinkedList<>();
          JSONArray assignees=jsonarray.getJSONObject(i).getJSONArray("assignees");
          for(int j=0;j<assignees.length();j++){
            assignedUsers.add((String)assignees.getJSONObject(j).get("login"));
          }
          builder.setAssignedUsers(assignedUsers);
          issues.add(builder.createGithubIssues());
        }page++;
        count++;
      }
    }return issues;
  }

  //Return a better date format
  public String buildDateEnglish(String date)
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
}
