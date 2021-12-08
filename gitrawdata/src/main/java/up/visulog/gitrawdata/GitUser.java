package up.visulog.gitrawdata;
public class GitUser{
    private final String id;

    private String type;

    private String site_admin;

    private String company;

    private String location;

    private String email;

    private int followers;

    private int following;

    private String hireable;

    private final String createdAt;

    public String bio;

    private String twitterUserName;

    public GitUser(String id, String type, String site_admin, String company, String location, String email, int followers, int following,
        String hireable, String createdAt, String bio, String twitterUserName) {
        this.id=id;
        this.type=type;
        this.site_admin=site_admin;
        this.company=company;
        this.location=location;
        this.email=email;
        this.followers=followers;
        this.following=following;
        this.hireable=hireable;
        this.createdAt=createdAt;
        this.bio=bio;
        this.twitterUserName=twitterUserName;
    }
    public String getId() {
        return id;
    }
    /*public void setId(int s) {
        this.id=s;
    }*/
    public String getType() {
        return type;
    }
    public void setType(String s) {
        this.type=s;
    }
    public String getSite_admin() {
        return site_admin;
    }
    public void setSite_admin(String s) {
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
    public String getHireable() {
        return hireable;
    }
    public void sethireable(String h) {
        hireable=h;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    /*public void setCreatedAt(String c) {
        createdAt=c;
    }*/
    public String getBio() {
        return bio;
    }
    public void setBio(String b) {
        bio=b;
    }
    public String getTwitteruserName() {
        return twitterUserName;
    }
    public void setTwitterUserName(String s) {
        twitterUserName=s;
    }

   
}