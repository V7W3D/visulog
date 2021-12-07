package up.visulog.analyzer;
import up.visulog.gitrawdata.Month;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;



public class CountCommitsPerDayAndAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerDayAndAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Parsable> list) {
        var result = new Result();
        for (var parsable : list) {
            Commit commit = (Commit) parsable;
            String[] date=commit.date.split(" at ");
            HashMap<String,Integer> commitPerAuthorDefault=new HashMap<>();
            commitPerAuthorDefault.put(commit.author,0);
            HashMap<String,Integer> map=result.commitsPerDayAndAuthor.getOrDefault(date[0], commitPerAuthorDefault);
            var nb=map.getOrDefault(commit.author,0);
            map.put(commit.author,nb+1);
            result.commitsPerDayAndAuthor.put(date[0],map);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countCommitsPerDayAndAuthor")));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, HashMap<String, Integer>> commitsPerDayAndAuthor = new HashMap<>();

        Map<String, HashMap<String,Integer>> getCommitsPerDayAndAuthor() {
            return commitsPerDayAndAuthor;
        }

        @Override
        public String getResultAsString() {
            return commitsPerDayAndAuthor.toString();
        }

     
        public String getResultAsHtmlDiv() {
            LinkedList<Object> commitsList=toList(commitsPerDayAndAuthor);
            StringBuilder html = new StringBuilder("<div>commits per day and author: <ul>");
            int i=0;
            while(i<commitsList.size()){
                html.append("<li>").append((String)commitsList.get(i)).append(": </li><ul>");
                for(var commitsPerAuthor : ((HashMap<String,Integer>)(commitsList.get(i+1))).entrySet()){
                    html.append("<li>").append(commitsPerAuthor.getKey()).append(": ").append(commitsPerAuthor.getValue()).append("</li>");
                }
                html.append("</ul>");
                i+=2;
            }
            html.append("</ul></div>");
            return html.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            return "";
        }


        /*
        creer une LinkedList qui contient les elements de commits tries
        du plus recent au moins recent en mettant chaque Value apres sa Key
        */
        public static LinkedList<Object> toList(Map<String, HashMap<String,Integer>> commits){
            LinkedList<Object> res=new LinkedList<Object>();
        
            //le trie
            for(var item : commits.entrySet()){
                if(res.size()==0){
                    res.add(item.getKey());
                    res.add(item.getValue());
                }
                else{
                    int i=0;
                    while(i<res.size() && !estSuperieur(item.getKey(),(String)res.get(i))){
                        i+=2;
                    }
                    res.add(i,item.getKey());
                    res.add(i+1,item.getValue());
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
        public String getChartName() {
            return "";
        }
    }
}
