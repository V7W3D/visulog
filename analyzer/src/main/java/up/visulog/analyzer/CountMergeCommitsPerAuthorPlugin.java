package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;


public class CountMergeCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountMergeCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Parsable> list) {
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
        result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countMergeCommits")));
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
            StringBuilder head = new StringBuilder();
            for (var item : mergeCommitsPerAuthor.entrySet()) {
                head.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return head.toString();
        }

        @Override
        public String getResultAsStringdate() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
