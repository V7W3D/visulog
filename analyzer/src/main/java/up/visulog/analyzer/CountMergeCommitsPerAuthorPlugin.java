package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import up.visulog.gitrawdata.*;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;

public class CountMergeCommitsPerAuthorPlugin extends AnalyzerGitLogPlugin {

    public CountMergeCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        if(configuration==null)
            configuration = generalConfiguration;
    }

    protected Result processLog(List<Parsable> list) {
        var result = new Result();
        for (var parsable : list) {
            Commit mergeCommit = (Commit) parsable;
            if(mergeCommit.mergedFrom != null){
                var nb = result.mergeCommitsPerAuthor.getOrDefault(mergeCommit.author, 0);
                result.mergeCommitsPerAuthor.put(mergeCommit.author, nb + 1);
            }
        }
        return result;
    }
    @Override
    public void run() {
        if(listCommits==null)        
            result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countMergeCommitsPerAuthor")));
        else
            result = processLog(listCommits);
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> mergeCommitsPerAuthor = new HashMap<>();

        Map<String, Integer> getMergeCommitsPerAuthor() {
            return mergeCommitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return mergeCommitsPerAuthor.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : mergeCommitsPerAuthor.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Merge commits per author: <ul>");
            for (var item : mergeCommitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "Merge Commits Per Author";
        }
    }
}
