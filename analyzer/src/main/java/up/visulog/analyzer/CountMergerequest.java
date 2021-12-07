package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountMergerequest implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    public static int nbcommits=0;

    public CountMergerequest(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        for (var commit : gitLog) {

            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);

            // lister les dates
            var nb1 = result.mergeRequest.getOrDefault(commit.mergedFrom, 0);
            result.mergeRequest.put(commit.mergedFrom, nb + 1);
        }


        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {

        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();
        private final Map<String, Integer> mergeRequest = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        Map<String, Integer> getCommitsdate() {
            return mergeRequest;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        @Override
        public String getResultAsStringdate() {

            return commitsPerAuthor.toString();
        }




        @Override
        public String getResultAsHtmlDiv() {
            System.out.println("Liste Merge Request :");
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            for (var item : mergeRequest.entrySet() ){
                System.out.println("-"+item.getKey()+"       "+item.getValue());
            }


            for (var item : commitsPerAuthor.entrySet() ){
                System.out.println("-"+item.getKey()+"       "+item.getValue());
            }


            return html.toString();

        }


        @Override
        public String GrapheJavaFxCanvaJs() {

            return "";
        }


        @Override
        public String getHTMLCommitAuth() {

            return "";
        }


    }
}
