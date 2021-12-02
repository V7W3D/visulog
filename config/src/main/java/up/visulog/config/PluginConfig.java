package up.visulog.config;

import java.util.Map;

// TODO: define what this type should be (probably a Map: settingKey -> settingValue)
public interface PluginConfig {
    Map<String, String> config();
}
