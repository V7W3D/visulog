package up.visulog.gitrawdata;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class GitUser implements Parsable{
    private int id;

    private String type;

    private boolean site_admin;

    private String company;

    private String name;


    private String location;

    private String email;

    private int followers;

    private int following;


    private String createdAt;
    private String updatedAt;

    private int public_repos;


    private String twitterUserName;

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
        this.createdAt=createdAt;
        this.twitterUserName=twitterUserName;
        this.name=name;
        this.updatedAt=updatedAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int s) {
        this.id=s;
    }
    public String getType() {
        return type;
    }
    public void setType(String s) {
        this.type=s;
    }
    public boolean getSite_admin() {
        return site_admin;
    }
    public void setSite_admin(boolean s) {
        site_admin=s;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String s) {
        company=s;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String l) {
        location=l;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String e) {
        email=e;
    }
    public int getFollowers() {
        return followers;
    }
    public void setFollowers(int f) {
        followers=f;
    }
    public int getFollowing() {
        return following;
    }
    public void setFollowing(int f) {
        following=f;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }


    public String getTwitteruserName() {
        return twitterUserName;
    }
    public void setTwitterUserName(String s) {
        twitterUserName=s;
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



   
}