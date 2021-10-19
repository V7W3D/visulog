package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import javax.print.DocFlavor;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    public static int nbcommits=0;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
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
            // Ajout
            System.out.println("Page html");
            System.out.println(" ");
            System.out.println("Liste des personnes qui ont faits des commits :"+getResultAsString());
            System.out.println(" ");

            /*try{
                Desktop d = Desktop.getDesktop();
                d.browse(new URI("file:///home/khalifa/Documents/L3/PROJET/visulog/analyzer/build/reports/tests/test/index.html"));
            }catch (URISyntaxException e){

            }catch (IOException e){

            }*/
             int mbTotalcommits=0;
            for (var item: getCommitsPerAuthor().entrySet()) {
                mbTotalcommits+= item.getValue();
                System.out.println(item.getKey()+": "+""+item.getValue()+" commit(s) ");
                System.out.println(" ");
            }
            System.out.println("**Le nombre total de commits branche principale: "+mbTotalcommits);
            System.out.println(" ");
             //Ajout
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}
