package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Analyzer {
    private final Configuration config;

    public Analyzer(Configuration config) {
        this.config = config;
    }

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) {
            var pluginName = pluginConfigEntry.getKey();
            var pluginConfig = pluginConfigEntry.getValue();
            var plugin = makePlugin(pluginName, pluginConfig);
            plugin.ifPresent(plugins::add);
        }
        // TODO: try running them in parallel (A FAIRE : essayez de les exécuter en parallèle)
        for (var plugin: plugins) {
        	es.execute(() -> { plugin.run(); });
        }

        es.shutdown();

        try {
			while(!es.awaitTermination(1, TimeUnit.MINUTES)) {
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        // store the results together in an AnalyzerResult instance and return it
        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }

    // TODO: find a way so that the list of plugins is not hardcoded in this factory
    //(A FAIRE : trouver un moyen pour que la liste des plugins ne soit pas codée en dur dans cette usine)
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        switch (pluginName) {//ça c'est du hardcoding
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config));
            case "countMergeCommits" : return Optional.of(new CountMergeCommitsPerAuthorPlugin(config));
            case "countMergeCommitsPerDay" : return Optional.of(new CountMergeCommitsPerDayPlugin(config));
            case "countCommitsPerDay" : return Optional.of(new CountCommitsPerDayPlugin(config));
            case "countLinesAddedPerFile" : return Optional.of(new CountAddedLinesPerFile(config));
            case "countLinesDeletedPerFile" : return Optional.of(new CountDeletedLinesPerFile(config));
            default : return Optional.empty();
        }
    }

}
