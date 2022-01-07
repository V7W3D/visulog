package up.visulog.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GithubUserPluginConfig implements PluginConfig{
    private Set<String> pValues=new HashSet<>();

    public void addUser(String pValue){
        this.pValues.add(pValue);
    }
    
    public Map<String, String> config(){
        return null;
    }

    public Set<String> getUsersName(){
        return pValues;
    }
}
