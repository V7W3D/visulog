package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CountCommitParjours implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    public static int nbcommits=0;

    public CountCommitParjours(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        for (var commit : gitLog) {

            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);

            // lister les dates
            nb = result.commitsdate.getOrDefault(commit.date, 0);
            result.commitsdate.put(commit.date, nb + 1);
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
        private final Map<String, Integer> commitsdate = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        Map<String, Integer> getCommitsdate() {
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
        public String GrapheJavaFxCanvaJs() {
            return null;
        }


        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            //System.out.println(" ");
            //Thu Aug 27 00:35:19 2020 +0200
            LocalDateTime ldt = LocalDateTime.now();
            System.out.print("Date du jour: ");
            System.out.println(DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH).format(ldt));
            String jour = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH).format(ldt);
            String mois = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH).format(ldt);
            String date_jour = DateTimeFormatter.ofPattern("d", Locale.ENGLISH).format(ldt);
            String annee = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH).format(ldt);
            System.out.println("Liste des commits du jour: ");
            int mbTotalcommitsjour = 0;
            for (var item1 : getCommitsdate().entrySet()) {
                //Thu Aug 27 00:35:19 2020 +0200
                if (item1.getKey().contains(jour)
                        && item1.getKey().contains(mois)
                        && item1.getKey().contains(date_jour)
                        && item1.getKey().contains(annee)) {
                    mbTotalcommitsjour += item1.getValue();
                    System.out.println("-"+item1.getKey() + " : " + item1.getValue() + " commit(s)");
                    //add to my result list
                }

            }
            System.out.println("Total commit(s) jour : "+mbTotalcommitsjour);
            System.out.println(" ");





            return html.toString();
        }
        @Override
        public String getHTMLCommitAuth() {


            return "";
        }

    }
}
