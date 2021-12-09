package up.visulog.analyzer;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Parsable;


public abstract class AnalyzerGitLogPlugin implements AnalyzerPlugin{
    protected Configuration configuration;
    protected static List<Parsable> listCommits;
    protected Result result;

    public AnalyzerGitLogPlugin(Configuration generalConfiguration){
        configuration=generalConfiguration;
    }
    @Override
    public Result getResult() throws IOException,JSONException{
        if (result == null) run();
        return result;
    }

    protected abstract Result processLog(List<Parsable> list); 
}
