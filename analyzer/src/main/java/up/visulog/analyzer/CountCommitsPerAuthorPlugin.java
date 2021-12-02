package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

import java.util.*;

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    public static int nbcommits=0;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit commit = (Commit) parsable;
            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);
            result.commitsdate.put(commit.date, nb + 1);
        }


        return result;
    }

    @Override
    public void run() {        
        result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countCommits")));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {

        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();
        private final Map<String, Integer> commitsdate = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        Map<String, Integer> getCommitsPerAuth() {
            return commitsdate;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        @Override
        public String getResultAsStringdate() {
            return commitsdate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder head = new StringBuilder();
            for (var item : commitsPerAuthor.entrySet()) {
                head.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return head.toString();
        }
    }
}
