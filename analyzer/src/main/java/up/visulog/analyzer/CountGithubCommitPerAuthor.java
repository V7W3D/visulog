package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.GithubCommit;
import up.visulog.gitrawdata.Parsable;
import up.visulog.gitrawdata.Parsing;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.JSONException;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Parsing;

public class CountGithubCommitPerAuthor extends AnalyzerGitLogPlugin {
    

    public CountGithubCommitPerAuthor(Configuration generalConfiguration)
    {
        if(configuration==null)
            configuration = generalConfiguration;
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
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
        System.out.println("Je suis la");
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
        String param = pValue.substring(7,pValue.length());
        try {
            List<GithubCommit> commits = GithubCommit.getCommitsFromURL(param);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),"git log"));
        
    }

    @Override
    public Result getResult() {
        // TODO Auto-generated method stub
        return null;
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
        public String getResultAsStringdate() {
            // TODO Auto-generated method stub
            return null;
        }

    }



    
}
