package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.*;

public class GithubIssues implements Parsable {
  
  private String title;
  private int number;
  private String creationDate;
  private String state;
  private List<String> assignedUsers;
  
  private static List<Parsable> listIssues;
    // Limite les call de REST API
  public static final int limit=2;

  public GithubIssues(String title, int number, String creationDate, String state, List<String> assignedUsers) {
    this.title = title;
    this.number = number;
    this.creationDate = creationDate;
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
    return listIssues;
  }

  public List<Parsable> getIssuesFromURL(String pValue) throws JSONException, IOException{
    List<Parsable> issues= new ArrayList<Parsable>();
    try{
      JSONArray jsonarray = readJsonIssuesFromUrl("https://api.github.com/repos"+pValue+"/issues");
    }catch (Exception e){
      System.out.println("Probleme: "+e);
      return issues;
    }int page=1;
    int count=0;
    while(count<limit){
      JSONArray jsonarray = readJsonIssuesFromUrl("https://api.github.com/repos"+pValue+"/issues?page="+page+"&per_page=100");  
      if(!jsonarray.isEmpty()){
        for(int i=0;i<jsonarray.length();i++){
          String title=(String) jsonarray.getJSONObject(i).get("title");
          int number=(int) jsonarray.getJSONObject(i).get("number");
          String creationDate=(String) jsonarray.getJSONObject(i).get("created_at");
          String state=(String) jsonarray.getJSONObject(i).get("state");
          JSONArray assignees=jsonarray.getJSONObject(i).getJSONArray("assignees");
          for(int j=0;j<assignees.length();j++){
            assignedUsers.add((String)assignees.getJSONObject(j).get("login"));
          }listIssues.add(new GithubIssues(title, number, creationDate, state, assignedUsers));
        }page++;
        count++;
      }
    }return listIssues;
  }
}
