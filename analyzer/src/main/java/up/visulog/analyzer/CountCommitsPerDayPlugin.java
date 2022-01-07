package up.visulog.analyzer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import up.visulog.gitrawdata.*;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;

public class CountCommitsPerDayPlugin extends AnalyzerGitLogPlugin {

    public CountCommitsPerDayPlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit commit = (Commit) parsable;
            String myDate = commit.date;
            String day = myDate.split(" ")[0];
            var nb = result.commitsPerDay.getOrDefault(day, 0);
            result.commitsPerDay.put(day, nb + 1);
        }
        return result;
    }

    @Override
    public void run() {
        if(!configuration.githubOrNormal()){
            if(listCommits==null)        
                result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countCommitsPerDay")));
            else
                result = processLog(listCommits);
        }else{
            result=processLog(GithubCommit.getGithubCommits(configuration.getUrlProject()));
        }
    }
    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerDay = new HashMap<>();

        Map<String, Integer> getCommitsPerDayAndAuthor() {
            return commitsPerDay;
        }

        @Override
        public String getResultAsString() {
            return commitsPerDay.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : commitsPerDay.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            //affcihage avec trie des jours
            StringBuilder html = new StringBuilder("<div>Commits per day: <ul>");
            for(int i=0;i<7;i++){
                if(commitsPerDay.get(Day.dayTrie.get(i))!=null)
                    html.append("<li>").append(Day.dayTrie.get(i)).append(": ").append(commitsPerDay.get(Day.dayTrie.get(i))).append("</li>");
            }
            html.append("</ul></div><br>");
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "Commits Per Day";
        }
    }
}
