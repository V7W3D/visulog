package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.GithubCommit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

import java.util.*;

public class ListeMergedCommitid extends AnalyzerGitLogPlugin {
    public static int nbcommits=0;

    public ListeMergedCommitid(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit commit = (Commit) parsable;
            var nb = result.commits.getOrDefault(commit.author, 0);
            result.commits.put(commit.author, nb + 1);

            var nb1 = result.ListecomMerged.getOrDefault(commit.mergedFrom, 0);
            result.ListecomMerged.put(commit.mergedFrom, nb + 1);

        }
        return result;
    }

    @Override
    public void run() {
        if(!configuration.githubOrNormal()){
            if(listCommits==null)
                result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("ListeMergedCommitid")));
            else
                result = processLog(listCommits);
        }else{
            result=processLog(GithubCommit.getGithubCommits(configuration.getUrlProject()));
        }
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commits = new HashMap<>();
        private final Map<String, Integer> ListecomMerged = new HashMap<>();

        Map<String, Integer> getCommitsdate() {
            return ListecomMerged;
        }

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
            for (var item : ListecomMerged.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>List commits merged to develop: <ul>");
            int total  = 0;
            int mergerequest=0;
            for (var item : ListecomMerged.entrySet()) {
                html.append("<li>"+"Commit id : ").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
                total = total + item.getValue();
            }

            html.append("<li>Total commits merged : " + total+"</li>");
            html.append("</ul></div><br>");
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "List commits merged to develop";
        }
    }
}
