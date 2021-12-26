package up.visulog.gitrawdata;

public class GitUserBuilder {
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

    public GitUserBuilder(int id){
        this.id=id;
    }
    public void setType(String type){
        this.type=type;
    }
    public void setSiteAdmin(boolean site_admin){
        this.site_admin=site_admin;
    }
    public void setCompany(String company){
        this.company=company;
    }
    public void setname(String name){
        this.name=name;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setFollowers(int followers){
        this.followers=followers;
    }
    public void setFollowing(int following){
        this.following=following;
    }
    public void setCreatedAt(String createdAt){
        this.createdAt=createdAt;
    }
    public void setUptatedAt(String updatedAt){
        this.updatedAt=updatedAt;
    }
    public void setPublicRepos(int public_repos){
        this.public_repos=public_repos;
    }
    public void setTwitterUserName(String twitterUserName){
        this.twitterUserName=twitterUserName;
    }

    public GitUser creategitUser(){
        return new GitUser(id,type,site_admin,public_repos,company,name,location,email,followers,following,
                            createdAt,updatedAt,twitterUserName);
    }


}
