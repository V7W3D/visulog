package up.visulog.gitrawdata;

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

  
}
