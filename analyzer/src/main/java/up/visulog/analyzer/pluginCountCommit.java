package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class pluginCountCommit implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    public static int nbcommits=0;

    public pluginCountCommit(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        for (var commit : gitLog) {

            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);
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


        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }




        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");

            System.out.println("Liste des personnes qui ont faits des commits :"+getResultAsString()+"\n");
            System.out.println(" ");

            int mbTotalcommits=0;
            for (var item: getCommitsPerAuthor().entrySet()) {
                mbTotalcommits+= item.getValue();
            }
            System.out.println("**Le nombre total de commits est: "+mbTotalcommits);
            System.out.println(" ");
            //Ajout



            return html.toString();

        }

        @Override
        public String getResultAsStringdate() {
            return null;
        }


        @Override
        public String GrapheJavaFxCanvaJs() {


            return null;
        }


        @Override
        public String getHTMLCommitAuth() {

            return null;
        }


    }
}
