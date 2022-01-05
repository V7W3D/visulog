package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import up.visulog.gitrawdata.*;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Parsable;

public class ListeMergedCommit extends AnalyzerGitLogPlugin {

    public ListeMergedCommit(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    protected Result processLog(List<Parsable> list) {
        var result = new Result();
        for (var parsable : list) {
            Commit mergeCommit = (Commit) parsable;
            if(mergeCommit.mergedFrom != null){
                var nb = result.ListeMerge.getOrDefault(mergeCommit.author, 0);
                result.ListeMerge.put(mergeCommit.author, nb + 1);

                var nb1 = result.mergedlist.getOrDefault(mergeCommit.mergedFrom, 0);
                result.mergedlist.put(mergeCommit.mergedFrom, nb + 1);
            }
        }
        return result;
    }
    @Override
    public void run() {
        if(listCommits==null)
            result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("ListeMergedCommit")));
        else
            result = processLog(listCommits);
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> ListeMerge = new HashMap<>();
        private final Map<String, Integer> mergedlist = new HashMap<>();


        Map<String, Integer> getMergeCommitsPerAuthor() {
            return ListeMerge;
        }

        @Override
        public String getResultAsString() {
            return ListeMerge.toString();
        }

        Map<String, Integer> MergedRe() {
            return mergedlist;
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : mergedlist.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }
        int totale=0;
        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Liste commit merged: <ul>");
            for (var item : mergedlist.entrySet()) {
                html.append("<li>"+"Commit id : ").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
                totale+=item.getValue();
                System.out.println(item.getKey()+": "+item.getValue());
                System.out.println("Slurhjk,;b");
            }
            html.append("<li>"+"Total commit merged : "+totale+"</li>");
            html.append("</ul></div>");
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "Liste commit merged";
        }
    }
}
