package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

            // lister les dates
            var nb1 = result.commitsdate.getOrDefault(commit.date, 0);
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
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");

            // Ajout
            System.out.println("Page html");
            System.out.println(" ");
            System.out.println("Liste des personnes qui ont faits des commits :"+getResultAsString());


            /*try{
                Desktop d = Desktop.getDesktop();
                d.browse(new URI("file:///home/khalifa/Documents/L3/PROJET/visulog/analyzer/build/reports/tests/test/index.html"));
            }catch (URISyntaxException e){

            }catch (IOException e){

            }*/

             //Ajout

            Set<String> da = commitsdate.keySet();

            for (String data : da) {
                //System.out.println(String.format(data));
            }

            // nombre totale de commits branche principale et branche intermediaire acceder par un checkout
            int mbTotalcommits = 0;
            for (var item : getCommitsPerAuthor().entrySet()) {
                mbTotalcommits += item.getValue();
                System.out.println(item.getKey() + ": " + "" + item.getValue() + " commit(s) ");

            }
            System.out.println(" ");
            System.out.println("**Le nombre total de commits est: " + mbTotalcommits);
            //System.out.println(" ");
            //Thu Aug 27 00:35:19 2020 +0200
            LocalDateTime ldt = LocalDateTime.now();
            System.out.println(DateTimeFormatter.ofPattern("EEEE MM dd yyyy", Locale.ENGLISH).format(ldt));

            for (var item1 : getCommitsPerAuth().entrySet()) {
                //startsWith("")
                //if (item1.getKey().startsWith(DateTimeFormatter.ofPattern("EEE MM dd", Locale.ENGLISH).format(ldt))) {
                    if(item1.getKey().contains(DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH).format(ldt))
                            && item1.getKey().contains(DateTimeFormatter.ofPattern("MM", Locale.ENGLISH).format(ldt))
                            && item1.getKey().contains(DateTimeFormatter.ofPattern("dd", Locale.ENGLISH).format(ldt))
                            && item1.getKey().contains(DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH).format(ldt))) {
                            System.out.println(item1.getKey()+" "+item1.getValue()+" commits");
                        //add to my result list
                    }
                //Thu Aug 27 00:35:19 2020 +0200
                /*if (item1.getKey().contains("Thu")
                        && item1.getKey().contains("Aug")
                        && item1.getKey().contains("27")
                        && item1.getKey().contains("2020")) {
                    System.out.println(item1.getKey() + " " + item1.getValue() + " commits");
                    //add to my result list
                }*/


            }


               /*try{
                    FileOutputStream file = new FileOutputStream("/home/khalifa/Documents/L3/Donnees.txt");
                    ObjectOutputStream s = new ObjectOutputStream(file);
                      s.writeObject(mbTotalcommits);
                      s.close();
                      System.out.println("Ajout des donnees reussi\n");
                }catch (IOException e){
                    e.printStackTrace();
                }*/

            // Enregidtrement des dates de commits et le nombre de commits pour chaque date dans un fichier
            File fil = new File("/home/khalifa/Documents/L3/Donnees.txt");
            BufferedWriter bf = null;
            try {
                bf = new BufferedWriter(new FileWriter(fil));
                for (Map.Entry<String, Integer> entry : commitsdate.entrySet()) {
                    bf.write(entry.getKey() + ":" + entry.getValue()+" commit(s)");

                    bf.newLine();
                }

                bf.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    //always close the writer
                    bf.close();
                } catch (Exception e) {
                }
            }


        //Ajout
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}
