package up.visulog.gitrawdata;
public class GitUser{
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

    private int public_repos;


    private String twitterUserName;

    public GitUser(){

    }

    public GitUser(int id, String type, boolean site_admin,int public_repos, String company,String name, String location, String email, int followers, int following,
        String hireable, String createdAt, String bio, String twitterUserName) {
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

   
}