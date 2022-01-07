package up.visulog.analyzer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import up.visulog.gitrawdata.*;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Month;
import up.visulog.gitrawdata.Parsable;

public class CountCommitsPerDatePlugin extends AnalyzerGitLogPlugin {

    public CountCommitsPerDatePlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit mergeCommit = (Commit) parsable;
           
            String[] date=mergeCommit.date.split(" at ");
            var nb=result.commitsPerDate.getOrDefault(date[0],0);
            result.commitsPerDate.put(date[0], nb+1);
        }
        return result;
    }

    @Override
    public void run() {
        if(!configuration.githubOrNormal()){
            if(listCommits==null)        
                result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countCommitsPerDate")));
            else
                result = processLog(listCommits);
        }else{
            result=processLog(GithubCommit.getGithubCommits(configuration.getUrlProject()));
        } 
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerDate = new HashMap<>();

        Map<String, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }

        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        public String getResultAsDataPoints() {
             //avec trie des dates
             LinkedList<String> commitsList=toList(commitsPerDate);
             StringBuilder dataPoints = new StringBuilder();
             int i=0;
             while(i<commitsList.size()){
                 dataPoints.append("{ label: '").append(commitsList.get(i)).append("', y: ").append(commitsList.get(i+1)).append("},");
                 i+=2;
             }
             return dataPoints.toString();
        }


        @Override
        public String getResultAsHtmlDiv() {
            LinkedList<String> commitsList=toList(commitsPerDate);
            StringBuilder html = new StringBuilder("<div>Commits per date: <ul>");
            int i=0;
            while(i<commitsList.size()){
                html.append("<li>").append(commitsList.get(i)).append(": ").append(commitsList.get(i+1)).append("</li>");
                i+=2;
            }
            html.append("</ul></div><br>");
            return html.toString();
        }

         /*
        creer une LinkedList qui contient les elements de mergeCommits tries
        du plus recent au moins recent en mettant chaque Value apres sa Key
        */
        public static LinkedList<String> toList(Map<String,Integer> mergeCommits){
            LinkedList<String> res=new LinkedList<String>();
        
            //le trie
            for(var item : mergeCommits.entrySet()){
                if(res.size()==0){
                    res.add(item.getKey());
                    res.add(item.getValue().toString());
                }
                else{
                    int i=0;
                    while(i<res.size() && !estSuperieur(item.getKey(),(String)res.get(i))){
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
        public String getChartName() {
            return "Commits Per Date";
        }
    }
}

