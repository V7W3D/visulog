package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.GithubCommit;
import up.visulog.gitrawdata.Parsable;
import java.io.*;
import java.util.*;

import org.json.JSONException;

public class CountGithubCommitPerAuthor implements AnalyzerPlugin {
    private static Configuration configuration;
    private Result result;

    public CountGithubCommitPerAuthor(Configuration generalConfiguration){
        configuration = generalConfiguration;
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        System.out.println("Arrivé ici et taille est "+gitLog.size());
        // for (var parsable : gitLog) {
        //     Commit commit = (Commit) parsable;
        //     var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
        //     result.commitsPerAuthor.put(commit.author, nb + 1);
        //     result.commitsdate.put(commit.date, nb + 1);
        // }


        return result;
    }

    @Override
    public void run() {
        // System.out.println(configuration.getPluginConfigs().size());
        String pValue = "";

        for (var pluginConfigEntry: configuration.getPluginConfigs().entrySet()) {
            String pluginName = pluginConfigEntry.getKey();
            if(pluginName.substring(0, 8).compareTo("commits/") == 0)
            {
                pValue = pluginName;
                break;
            }
        }
        System.out.println("pValue = " + pValue);
        // System.out.println("pValue coupé = " + pValue.substring(7,pValue.length()));
        List<Parsable> commits = new ArrayList<Parsable>();
        String param = pValue.substring(7,pValue.length());
        try {
            commits = GithubCommit.getCommitsFromURL(param);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        result = processLog(commits);
        // result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),"git log"));
        
    }

      @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {

        @Override
        public String getResultAsString() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getResultAsHtmlDiv() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getResultAsDataPoints(){
            return "";
        }
       
        @Override
        public String getChartName(){
            return "";
        }

    }



    
}
