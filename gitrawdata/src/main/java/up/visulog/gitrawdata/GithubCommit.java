package up.visulog.gitrawdata;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GithubCommit {
  
  public final String id;
  public final String date;
  public final String author;
  public final String description;

  public GithubCommit(String id, String author, String date, String description) {
    this.id = id;
    this.author = author;
    this.date = date;
    this.description = description;
  }

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
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

  public static List<GithubCommit> getCommitsFromURL(String pValue) throws JSONException, IOException
  {
    List<GithubCommit> commits = new ArrayList<GithubCommit>();
    int page=1;
    int count=0;
    int last=1;
    try {
      JSONArray jsonarray = readJsonCommitsFromUrl("https://api.github.com/repos"+pValue+"/commits");
    } catch (Exception e) {
      System.out.println("Probleme "+e);
      return commits;
    }
    while(last<=10){
      JSONArray jsonarray = readJsonCommitsFromUrl("https://api.github.com/repos/torvalds/linux/commits?page="+page+"&per_page=100");
      for(int i=0; i<jsonarray.length(); i++){
        String author = (String) jsonarray.getJSONObject(i).getJSONObject("commit").getJSONObject("author").get("name");
        String id = (String) jsonarray.getJSONObject(0).get("node_id");
        String description =(String) jsonarray.getJSONObject(0).getJSONObject("commit").get("message");
        
        String date = (String)jsonarray.getJSONObject(i).getJSONObject("commit").getJSONObject("author").get("date");
        count++;
        // System.out.println("donnéees "+author+" "+id+" "+description+" "+" date est "+date);
        commits.add(new GithubCommit(author, id, description, date));
      }last++;
      page++;
    }
    return commits;
  }

  
}
