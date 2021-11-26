package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;


public class CountMergeCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountMergeCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        for (var mergeCommit : gitLog) {
            if(mergeCommit.mergedFrom != null){
                var nb = result.mergeCommitsPerAuthor.getOrDefault(mergeCommit.author, 0);
                result.mergeCommitsPerAuthor.put(mergeCommit.author, nb + 1);
            }
        }
        return result;
    }

    @Override
    public void run() {
        //result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
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
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Merge commits per author: <ul>");
            for (var item : mergeCommitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }

        @Override
        public String getResultAsStringdate() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
