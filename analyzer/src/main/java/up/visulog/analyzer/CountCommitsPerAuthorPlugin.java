package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

import java.util.*;

public class CountCommitsPerAuthorPlugin extends AnalyzerGitLogPlugin {
    public static int nbcommits=0;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit commit = (Commit) parsable;
            var nb = result.commits.getOrDefault(commit.author, 0);
            result.commits.put(commit.author, nb + 1);
        }
        return result;
    }
    @Override
    public void run() {
        if(listCommits==null)        
            result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countCommits")));
        else
            result = processLog(listCommits);
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commits = new HashMap<>();

        Map<String, Integer> getCommitsPerAuth() {
            return commits;
        }

        @Override
        public String getResultAsString() {
            return commits.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : commits.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }
        
        @Override
        public String getResultAsHtmlDiv() {
	    StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
	    int total  = 0;
            for (var item : commits.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
                total = total + item.getValue();
            }
            html.append("</ul></div>");
            html.append("Total commits : " + total);
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "Commits Per Author";
        }
    }
}
