package up.visulog.analyzer;

import java.util.LinkedList;
import java.util.List;

import up.visulog.gitrawdata.GithubIssues;
import up.visulog.gitrawdata.Parsable;
import up.visulog.config.Configuration;

public class GithubIssuesPlugin implements AnalyzerPlugin{
    private Configuration configuration;
    private Result result;

    public GithubIssuesPlugin(Configuration generalConfiguration) {
            configuration = generalConfiguration;
    }

    protected Result processLog(List<Parsable> githubIssues) {
        var result = new Result();
        for(Parsable issue : githubIssues){
            result.issues.add((GithubIssues)issue);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(GithubIssues.getGithubIssues(configuration.getUrlProject()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }
    
    static class Result implements AnalyzerPlugin.Result {
        List<GithubIssues> issues=new LinkedList<>();

        List<GithubIssues> getUser() {
            return issues;
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
            for(GithubIssues issue : issues){
                html.append("<div> Issue :<ul>"); 
                html.append("<li>").append("Number: ").append(issue.number).append("</li>");
                html.append("<li>").append("Title: ").append(issue.title).append("</li>");
                
                html.append("<li>").append("Creation date: ").append(issue.creationDate).append("</li>");
                html.append("<li>").append("State: ").append(issue.state).append("</li>");
                html.append("<li>Assigned users : </li><ul>");
                for(String assigned : issue.assignedUsers)
                    html.append("<li>").append("Assigned user: ").append(assigned).append("</li>");
                html.append("</ul></ul></div><br>");
            }
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "Issues";
        }
    }
}
