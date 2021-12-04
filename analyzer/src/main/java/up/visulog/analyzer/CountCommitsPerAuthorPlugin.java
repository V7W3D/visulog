package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

import java.util.*;

public class CountCommitsPerAuthorPlugin extends AnalyzerGitLogPlugin {
    public static int nbcommits=0;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        if(configuration==null)
            configuration = generalConfiguration;
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit commit = (Commit) parsable;
            var nb = result.commitsPerDay.getOrDefault(commit.author, 0);
            result.commitsPerDay.put(commit.author, nb + 1);
        }


        return result;
    }

    @Override
    public void run() {        
        result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countCommits")));
    }


    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerDay = new HashMap<>();

        Map<String, Integer> getCommitsPerAuth() {
            return commitsPerDay;
        }

        @Override
        public String getResultAsString() {
            return commitsPerDay.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : commitsPerDay.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }

        @Override
        public String getChartName() {
            return "Commits Per Author";
        }
    }
}
