package up.visulog.analyzer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Month;

public class CountMergeCommitsPerDayPlugin implements AnalyzerPlugin{

    private final Configuration configuration;
    private Result result;

    public CountMergeCommitsPerDayPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        for (var commit : gitLog) {
            if(commit.mergedFrom != null){
                String[] date=commit.date.split("at");
                var nb = result.mergeCommitsPerDate.getOrDefault(date[0], 0);
                result.mergeCommitsPerDate.put(date[0], nb + 1);
            }
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
        private final Map<String, Integer> mergeCommitsPerDate = new HashMap<>();

        Map<String, Integer> getMergeCommitsPerAuthor() {
            return mergeCommitsPerDate;
        }

        @Override
        public String getResultAsString() {
            return mergeCommitsPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            LinkedList<String> mergeCommitsList=toList(mergeCommitsPerDate);
            StringBuilder html = new StringBuilder("<div>Merge commits per Day: <ul>");
            int i=0;
            while(i<mergeCommitsList.size()) {
                html.append("<li>").append(mergeCommitsList.get(i)).append(": ").append(mergeCommitsList.get(i+1)).append("</li>");
                i+=2;
            }
            html.append("</ul></div>");
            return html.toString();
        }

        /*
        creer une LinkedList qui contient les elements de mergeCommits tries
        du plus recent au moins recent en mettant chaque Value apres sa Key
        */
        public static LinkedList<String> toList(Map <String, Integer> mergeCommits){
            LinkedList<String> res=new LinkedList<String>();
        
            //le trie
            for(var item : mergeCommits.entrySet()){
                if(res.size()==0){
                    res.add(item.getKey());
                    res.add(item.getValue().toString());
                }
                else{
                    int i=0;
                    while(i<res.size() && !estSuperieur(item.getKey(),res.get(i))){
                        i+=2;
                    }
                    res.add(i,item.getKey());
                    res.add(i+1,item.getValue().toString());
                }
            }
            return res;
        }

        /*
        renvoie true si d1 est plus recente que d2, ou d1=d2
        renvoie false sinon
         */
        public static boolean estSuperieur(String d1, String d2){
            String[] d1Split=d1.split(" ");
            String[] d2Split=d2.split(" ");
            //comparer les annees
            if(Integer.parseInt(d1Split[5])>Integer.parseInt(d2Split[5]))
                return true;
            if(Integer.parseInt(d1Split[5])<Integer.parseInt(d2Split[5]))
                return false;
            //comparer les mois
            if(Month.estSuperieur(d1Split[4],d2Split[4])==1)
                return true;
            if(Month.estSuperieur(d1Split[4],d2Split[4])==-1)
                return false;
            //comparer les jours
            int size1=d1Split[2].length();
            int size2=d2Split[2].length();
            if(Integer.parseInt(d1Split[2].substring(0,size1-2))>Integer.parseInt(d2Split[2].substring(0,size2-2)))
                return true;
            if(Integer.parseInt(d1Split[2].substring(0,size1-2))<Integer.parseInt(d2Split[2].substring(0,size2-2)))
                return false;
            return true;//on n'aura jamais deux dates egales, donc on n'arrivera pas ici
        }

        @Override
        public String getResultAsStringdate() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
