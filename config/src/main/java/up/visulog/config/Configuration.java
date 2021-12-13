package up.visulog.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Configuration {
    private boolean githubProjects;
    private String urlProject;
    private final Path gitPath;
    private final Map<String, PluginConfig> plugins;

    public Configuration(Path gitPath, Map<String, PluginConfig> plugins, boolean githubProjects, String url) {
        this.gitPath = gitPath;
        this.plugins = Map.copyOf(plugins);
        this.githubProjects=githubProjects;
        urlProject=url;
    }

    public Path getGitPath() {
        return gitPath;
    }

    public Map<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }

    public boolean githubOrNormal(){
        return githubProjects;
    }

    public String getUrlProject(){
        return urlProject;
    }

    public ArrayList<String> buildCommand(String pluginName){
        ArrayList<String> command = new ArrayList<String>();
        Map<String, String> pluginConfig = getPluginConfigs().get(pluginName).config();
        Iterator<Map.Entry<String, String>> it = pluginConfig.entrySet().iterator();
        command.add(it.next().getValue());
        while(it.hasNext()){
            command.add(it.next().getValue());
        }
        return command;
    }
}
