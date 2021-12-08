package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.File;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

public class CountDeletedLinesPerFile implements AnalyzerPlugin {
    private static Configuration configuration;
    private Result result;

    public CountDeletedLinesPerFile(Configuration generalConfiguration) {
        configuration = generalConfiguration;
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            File file = (File) parsable;
            var nb = result.linesDeletedPerFile.getOrDefault(file.name, 0);
            result.linesDeletedPerFile.put(file.name, nb + file.linesDeleted);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countLinesDeletedPerFile")));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }
    
    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> linesDeletedPerFile = new HashMap<>();

        Map<String, Integer> getLinesAddedPerFile() {
            return linesDeletedPerFile;
        }

        @Override
        public String getResultAsString() {
            return linesDeletedPerFile.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : linesDeletedPerFile.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Lines Deleted Per File :<ul>");
            for (var item : linesDeletedPerFile.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "Lines Deleted Per File";
        }
    }
}
