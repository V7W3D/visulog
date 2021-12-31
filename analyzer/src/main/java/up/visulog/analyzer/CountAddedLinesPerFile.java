package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.File;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

public class CountAddedLinesPerFile implements AnalyzerPlugin {
    private static Configuration configuration;
    private Result result;

    public CountAddedLinesPerFile(Configuration generalConfiguration) {
            configuration = generalConfiguration;
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            File file = (File) parsable;
            var nb = result.linesAddedPerFile.getOrDefault(file.name, 0);
            result.linesAddedPerFile.put(file.name, nb + file.linesAdded);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("countLinesAddedPerFile")));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }
    
    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> linesAddedPerFile = new HashMap<>();

        Map<String, Integer> getLinesAddedPerFile() {
            return linesAddedPerFile;
        }

        @Override
        public String getResultAsString() {
            return linesAddedPerFile.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();
            for (var item : linesAddedPerFile.entrySet()) {
                dataPoints.append("{ label: '").append(item.getKey()).append("', y: ").append(item.getValue()).append("},");
            }
            return dataPoints.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Lines Added Per File :<ul>");
            for (var item : linesAddedPerFile.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div><br>");
            return html.toString();
        }

        @Override
        public String getChartName() {
            return "Lines Added Per File";
        }
    }
}
