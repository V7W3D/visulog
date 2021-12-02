package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

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
        result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),"git log"));
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
            // Ajout
            System.out.println("Page html");
            System.out.println(" ");
            System.out.println("Liste des personnes qui ont faits des commits :"+getResultAsString());

            //Ajout

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
            System.out.print("Date du jour: ");
            System.out.println(DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH).format(ldt));
            String jour = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH).format(ldt);
            String mois = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH).format(ldt);
            String date_jour = DateTimeFormatter.ofPattern("d", Locale.ENGLISH).format(ldt);
            String annee = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH).format(ldt);
            System.out.println("Liste des commits du jour: ");
            int mbTotalcommitsjour = 0;
            for (var item1 : getCommitsPerAuth().entrySet()) {
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

            // Enregidtrement des dates de commits et le nombre de commits pour chaque date dans un fichier
            File fildc = new File("/home/khalifa/Documents/L3/Donnees.txt");
            BufferedWriter bfdc = null;
            try {
                bfdc = new BufferedWriter(new FileWriter(fildc));
                //
                for (Map.Entry<String, Integer> entry: commitsdate.entrySet()) {
                    bfdc.write(entry.getKey() + " : " + entry.getValue()+" commit(s)");
                    bfdc.newLine();
                }

                bfdc.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    //always close the writer
                    bfdc.close();
                } catch (Exception e) {
                }
            }



            File filecomdeuM = new File("/home/khalifa/Documents/L3/commits2021.txt");
            File filecomdeuMdate = new File("/home/khalifa/Documents/L3/Date.txt");
            BufferedWriter bfcomdeuM= null;
            BufferedWriter bfcomdeuMdate= null;
            try {
                bfcomdeuM = new BufferedWriter(new FileWriter(filecomdeuM));
                bfcomdeuMdate = new BufferedWriter(new FileWriter(filecomdeuMdate));
                //


                        for (Map.Entry<String, Integer> entry: commitsdate.entrySet()) {
                            if (entry.getKey().contains(DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH).format(ldt))) {
                                bfcomdeuM.write(entry.getKey() + " : " + entry.getValue()+" commit(s)");
                                bfcomdeuM.newLine();
                                bfcomdeuMdate.write(entry.getKey());
                                bfcomdeuMdate.newLine();

                            }
                        }

                bfcomdeuM.flush();
                bfcomdeuM.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    //always close the writer
                    bfcomdeuM.close();
                    bfcomdeuMdate.close();

                } catch (Exception e) {
                }
            }
            
        //Ajout
            int total  = 0;
            for (var item : commitsPerAuthor.entrySet()) {
                head.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
                total = total + item.getValue();
            }
            return head.toString();
        }
    }
}
