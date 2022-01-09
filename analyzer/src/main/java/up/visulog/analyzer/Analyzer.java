package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONException;

public class Analyzer {
    private final Configuration config;

    public Analyzer(Configuration config) {
        this.config = config;
    }

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>();
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) {
            var pluginName = pluginConfigEntry.getKey();
            var pluginConfig = pluginConfigEntry.getValue();
            var plugin = makePlugin(pluginName, pluginConfig);
            plugin.ifPresent(plugins::add);
        }
        // TODO: try running them in parallel (A FAIRE : essayez de les exécuter en parallèle)
        for (var plugin: plugins)
            try {
                plugin.run();
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        // store the results together in an AnalyzerResult instance and return it
        return new AnalyzerResult(plugins.stream().map(t -> {
            try {
                return t.getResult();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }

    // TODO: find a way so that the list of plugins is not hardcoded in this factory
    //(A FAIRE : trouver un moyen pour que la liste des plugins ne soit pas codée en dur dans cette usine)
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        switch (pluginName) {//ça c'est du hardcoding
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config));
            case "countMergeCommits" : return Optional.of(new CountMergeCommitsPerAuthorPlugin(config));
            case "ListeMergedCommitid" : return Optional.of(new ListeMergedCommitid(config));

            case "Issues" : return Optional.of(new PluginIssuesGitlab(config));

            case "countMergeCommitsPerDay" : return Optional.of(new CountMergeCommitsPerDayPlugin(config));
            case "countCommitsPerDay" : return Optional.of(new CountCommitsPerDayPlugin(config));
            case "countMergeCommitsPerDate" : return Optional.of(new CountMergeCommitsPerDatePlugin(config));
            case "countCommitsPerDate" : return Optional.of(new CountCommitsPerDatePlugin(config));
            case "countCommitsPerDateAndAuthor" : return Optional.of(new CountCommitsPerDateAndAuthorPlugin(config));
            case "countMergeCommitsPerDateAndAuthor" : return Optional.of(new CountMergeCommitsPerDateAndAuthorPlugin(config));
            case "countLinesAddedPerFile" : return Optional.of(new CountAddedLinesPerFile(config));
            case "countLinesDeletedPerFile" : return Optional.of(new CountDeletedLinesPerFile(config));
            case "githubUser" : return Optional.of(new GithubUserPlugin(pluginConfig));
            case "githubIssues" : return Optional.of(new GithubIssuesPlugin(config));
            default : return Optional.empty();
        }
    }

}
