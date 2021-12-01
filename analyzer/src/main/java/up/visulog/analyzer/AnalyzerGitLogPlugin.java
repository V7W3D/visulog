package up.visulog.analyzer;

import java.util.List;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

public abstract class AnalyzerGitLogPlugin implements AnalyzerPlugin{
    protected static Configuration configuration;
    protected static List<Parsable> listCommits;
    protected Result result;

    @Override
    public void run() {
        if(listCommits==null)
            listCommits=Parsing.parseLogFromCommand(configuration.getGitPath(),"git log");
        
        result = processLog(listCommits);
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    protected abstract Result processLog(List<Parsable> list); 
}
