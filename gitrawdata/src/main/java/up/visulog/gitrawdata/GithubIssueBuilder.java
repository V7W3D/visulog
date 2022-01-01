package up.visulog.gitrawdata;

import java.util.List;

public class GithubIssueBuilder {
    private String title;
    private int number;
    private String creationDate;
    private String state;
    private List<String> assignedUsers;


    public void setTitle(String title){
        this.title=title;
    }
    public void setNumber(int number){
        this.number=number;
    }
    public void setCreationDate(String creationDate){
        this.creationDate=creationDate;
    }
    public void setState(String state){
        this.state=state;
    }
    public void setAssignedUsers(List<String> assignedUsers){
        this.assignedUsers=assignedUsers;
    }

    public GithubIssues createGithubIssues(){
        return new GithubIssues(title, number, creationDate, state, assignedUsers);
    }

}
