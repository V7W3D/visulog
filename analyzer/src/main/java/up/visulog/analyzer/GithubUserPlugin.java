package up.visulog.analyzer;
import java.util.LinkedList;
import java.util.List;

import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.GitUser;
import up.visulog.gitrawdata.Parsable;
import up.visulog.config.GithubUserPluginConfig;

public class GithubUserPlugin implements AnalyzerPlugin{
    private PluginConfig configuration;
    private Result result;

    public GithubUserPlugin(PluginConfig config) {
            configuration = config;
    }

    protected Result processLog(List<Parsable> users) {
        var result = new Result();
        for(Parsable user : users){
            result.users.add((GitUser)user);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(GitUser.getGitUsers(((GithubUserPluginConfig)configuration).getUsersName()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }
    
    static class Result implements AnalyzerPlugin.Result {
        List<GitUser> users=new LinkedList<>();

        List<GitUser> getUser() {
            return users;
        }

        @Override
        public String getResultAsString() {
            return "";
        }

        @Override
        public String getResultAsDataPoints() {
            return "";
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder(); 
            for(GitUser user : users){
                html.append("<div>" + user.name + " :<ul>"); 
                html.append("<li>").append("Id: ").append(user.id).append("</li>");
                html.append("<li>").append("Type: ").append(user.type).append("</li>");
                html.append("<li>").append("Site admin: ").append(user.site_admin).append("</li>");
                html.append("<li>").append("Public repos: ").append(user.public_repos).append("</li>");
                html.append("<li>").append("Company: ").append(user.company).append("</li>");
                html.append("<li>").append("Location: ").append(user.location).append("</li>");
                html.append("<li>").append("Followers: ").append(user.followers).append("</li>");
                html.append("<li>").append("Following: ").append(user.following).append("</li>");
                html.append("<li>").append("Created at: ").append(user.createdAt).append("</li>");
                html.append("<li>").append("Updated at: ").append(user.updatedAt).append("</li>");
                html.append("<li>").append("Twiter user name: ").append(user.twitterUserName).append("</li>");
                html.append("</ul></div><br>");
            }
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "User";
        }
    }
}
