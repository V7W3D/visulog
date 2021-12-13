package up.visulog.analyzer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import up.visulog.gitrawdata.*;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;

public class CountMergeCommitsPerDayPlugin extends AnalyzerGitLogPlugin {

    public CountMergeCommitsPerDayPlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }


    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit mergeCommit = (Commit) parsable;
            if(mergeCommit.mergedFrom != null){
                String myDate = mergeCommit.date;
                String day = myDate.split(" ")[0];
                var nb = result.mergeCommitsPerDay.getOrDefault(day, 0);
                result.mergeCommitsPerDay.put(day, nb + 1);
            }
        }
        return result;
    }

    @Override
    public void run() {
        
        if(listCommits==null)        
            result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countMergeCommitsPerDay")));
        else
            result = processLog(listCommits);   
    }
    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> mergeCommitsPerDay = new HashMap<>();

        Map<String, Integer> getCommitsPerDayAndAuthor() {
            return mergeCommitsPerDay;
        }

        @Override
        public String getResultAsString() {
            return mergeCommitsPerDay.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : mergeCommitsPerDay.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }


        @Override
        public String getResultAsHtmlDiv() {
            //affcihage avec trie des jours
            StringBuilder html = new StringBuilder("<div>Merge commits per day: <ul>");
            for(int i=0;i<7;i++){
                if(mergeCommitsPerDay.get(Day.dayTrie.get(i))!=null)
                    html.append("<li>").append(Day.dayTrie.get(i)).append(": ").append(mergeCommitsPerDay.get(Day.dayTrie.get(i))).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        @Override
        public String getChartName() {
            return "Merge Commits Per Day";
        }
    }
}
