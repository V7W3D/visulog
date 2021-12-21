package up.visulog.gitrawdata;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class GitUser implements Parsable{
    public int id;
    public String type;
    public boolean site_admin;
    public String company;
    public String name;
    public String location;
    public String email;
    public int followers;
    public int following;
    public String createdAt;
    public String updatedAt;
    public int public_repos;
    public String twitterUserName;

    public GitUser(){

    }

    public GitUser(int id, String type, boolean site_admin,int public_repos, String company,String name, String location, String email, int followers, int following,
         String createdAt,String updatedAt, String twitterUserName) {
        this.id=id;
        this.type=type;
        this.site_admin=site_admin;
        this.company=company;
        this.location=location;
        this.email=email;
        this.followers=followers;
        this.following=following;
        this.public_repos=public_repos;
        this.createdAt=buildDateEnglish(createdAt);
        this.twitterUserName=twitterUserName;
        this.name=name;
        this.updatedAt=buildDateEnglish(updatedAt);
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
  
    @Override
    public String toString() {
        return "GitUser [company=" + company + ", createdAt=" + createdAt + ", email=" + email + ", followers="
                + followers + ", following=" + following + ", id=" + id + ", location=" + location + ", name=" + name
                + ", public_repos=" + public_repos + ", site_admin=" + site_admin + ", twitterUserName="
                + twitterUserName + ", type=" + type + ", updatedAt=" + updatedAt + "]";
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

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }

    public static List<Parsable> getGitUsers(Set<String> pValues){
        LinkedList<Parsable> res=new LinkedList<>();
        for(String s : pValues){
            Optional<Parsable> user=getGitUser(s);
            if(user.isPresent())
                res.add(user.get());
        }
        return res;
    }

    public static Optional<Parsable> getGitUser(String pValue) throws JSONException
    {
      JSONObject json;
      try{
        json = readJsonFromUrl("https://api.github.com/users/" + pValue);

        int  id = (Integer)json.get("id");
        GitUserBuilder builder=new GitUserBuilder(id);
        builder.setType((String)json.get("type"));
        builder.setSiteAdmin((boolean)json.get("site_admin"));
        String company= json.isNull("company") ? "" : (String)json.get("company");
        builder.setCompany(company);
        String name = json.isNull("name") ? "" : (String)json.get("name");
        builder.setname(name);
        String location = json.isNull("location") ? "" : (String)json.get("location");
        builder.setLocation(location);
        String email = json.isNull("email") ? "" : (String)json.get("email");
        builder.setEmail(email);
        int followers = (int)json.get("followers");
        builder.setFollowers(followers);
        int following = (int)json.get("following");
        builder.setFollowing(following);
        String createdAt = GithubCommit.toGitLogDate((String) json.get("created_at"));
        builder.setCreatedAt(createdAt);
        String updatedAt = GithubCommit.toGitLogDate((String) json.get("updated_at"));
        builder.setUptatedAt(updatedAt);
        int public_repos = (int)json.get("public_repos");
        builder.setPublicRepos(public_repos);
        String twitterUserName = json.isNull("twitter_user_name") ? "" : (String)json.get("twitter_user_name");
        builder.setTwitterUserName(twitterUserName);
        return Optional.of(builder.creategitUser());
      }catch (Exception e){
        System.out.println(e);
        return Optional.empty();
      }
    }


   
}