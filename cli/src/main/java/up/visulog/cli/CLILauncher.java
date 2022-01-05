package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.GithubUserPluginConfig;
import up.visulog.config.PluginConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CLILauncher {

    private static boolean textDisplay=true;//pas d'affichage en texte si false
    private static boolean graphDisplay=true;//pas d'affichage en graph si false
    private static boolean githubProjects=false;//avoir le projet github si true
    private static String urlProject=null;//l'url du project github
    private static GithubUserPluginConfig configGitUser=new GithubUserPluginConfig();//pour le plugin githubUser
    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            if(graphDisplay)
                results.toHTMLGraph();
            if(textDisplay)
                results.toHTML();
            //System.out.println(results.toHTML());
        } else displayHelpAndExit();
    }

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var gitPath = FileSystems.getDefault().getPath(".");
        var plugins = new HashMap<String, PluginConfig>();
        for (var arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length != 2) return Optional.empty();
                else {
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName) {
                        case "--addPlugin":
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:
                            PluginConfig GitLogPluginConfig = new PluginConfig(){
                                @Override
                                public Map<String, String> config() {
                                    Map<String, String> conf = new TreeMap<String, String>();
                                    conf.put("command", "git");
                                    conf.put("param1", "log");
                                    return conf;
                                }
                            };

                            PluginConfig FilePluginConfig =  new PluginConfig(){

                                @Override
                                public Map<String, String> config() {
                                    Map<String, String> conf = new TreeMap<String, String>();
                                    conf.put("command", "git");
                                    conf.put("param1", "whatchanged");
                                    conf.put("param2", "--numstat");
                                    conf.put("param3", "--pretty=");
                                    return conf;
                                }

                            };

                            switch(pValue) {
                                case "countCommits" : 
                                    plugins.put("countCommits", GitLogPluginConfig);
                                    break;
                                case "countMergeCommits" : 
                                    plugins.put("countMergeCommits", GitLogPluginConfig);
                                    break;
                                case "countCommitsPerDay" : 
                                    plugins.put("countCommitsPerDay", GitLogPluginConfig);
                                    break;
                                case "countMergeCommitsPerDay" : 
                                    plugins.put("countMergeCommitsPerDay", GitLogPluginConfig);
                                    break;
                                case "countCommitsPerDate" :
                                    plugins.put("countCommitsPerDate", GitLogPluginConfig);
                                    break;
                                case "countMergeCommitsPerDate" :
                                    plugins.put("countMergeCommitsPerDate", GitLogPluginConfig);
                                    break;
                                case "countCommitsPerDateAndAuthor" :
                                    plugins.put("countCommitsPerDateAndAuthor", GitLogPluginConfig);
                                    break;
                                case "countMergeCommitsPerDateAndAuthor" :
                                    plugins.put("countMergeCommitsPerDateAndAuthor", GitLogPluginConfig);
                                    break;

                                case "ListeMergedCommitid" :
                                    plugins.put("ListeMergedCommitid", GitLogPluginConfig);
                                    break;
                                case "countLinesAddedPerFile" :
                                    plugins.put("countLinesAddedPerFile", FilePluginConfig);
                                    break;
                                case "countLinesDeletedPerFile" : 
                                    plugins.put("countLinesDeletedPerFile", FilePluginConfig);
                                    break;
                            }
                            break;
                        case "--loadConfigFile":
                            // TODO (load options from a file)
                            try {
                                File myArgs = new File(pValue);
                                Scanner myReader = new Scanner(myArgs);
                                Optional<Configuration> makeConfigFromCommandLineArgs = Optional.empty();
                                if (myReader.hasNextLine()) {
                                    String data = myReader.nextLine();
                                    makeConfigFromCommandLineArgs = makeConfigFromCommandLineArgs(data.split(" "));
                                }
                                myReader.close();
                                return makeConfigFromCommandLineArgs;
                            } catch (FileNotFoundException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }
                            break;
                        case "--githubProjects":
                            githubProjects=true;
                            urlProject= pValue;
                            break;
                        case "--githubUser" :
                            configGitUser.addUser(pValue);
                            plugins.put("githubUser", configGitUser);
                            break; 
                        case "--githubIssues" :
                            PluginConfig githubIssuesConfig= new PluginConfig(){
                                @Override
                                public Map<String, String> config() {
                                    return null;
                                }
                            };
                            urlProject=pValue;
                            plugins.put("githubIssues", githubIssuesConfig);
                            break;
                        case "--justSaveConfigFile":
                            // TODO (save command line options to a file instead of running the analysis)
                            try {
                                File myObj = new File(pValue);
                                if (myObj.createNewFile()) {
                                  System.out.println("File created: " + myObj.getName());
                                } else {
                                  System.out.println("File already exists.");
                                }
                            } catch (IOException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }
                            try {
                                FileWriter myWriter = new FileWriter(pValue);
                                for(String a : args) {
                                    if(a.startsWith("--") && !a.contains("justSaveConfigFile")){
                                        myWriter.write(a);
                                    }
                                }
                                myWriter.close();
                                System.out.println("Successfully wrote to the file.");
                            } catch (IOException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }
                            break;
                        case "--graphDisplay" :
                            if(pValue.equals("true"))
                                graphDisplay=true;
                            else
                                graphDisplay=false;
                            break;
                        case "--textDisplay" :
                            if(pValue.equals("true"))
                                textDisplay=true;
                            else
                                textDisplay=false;
                            break;
                            case "--help" :
                            if(pValue.equals("all")){
                                System.out.println("All the commands available are :");
                            System.out.println("\n[--addPlugin] : this command will run the analysis of the argument's plugin. \n    Syntaxe : --addPlugin=[Argument]\n    Example : --addPlugin=countCommits");
                            System.out.println("\n    =countCommits will count the number of commits of each member of the git and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            System.out.println("\n    =countMergeCommits will count the number of merge commits of each member and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            System.out.println("\n    =countCommitsPerDay will count the number of commits of each member per day and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            System.out.println("\n    =countMergeCommitsPerDay will count the number of merge commits of each member per day and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a atext.");
                            System.out.println("\n    =countCommitsPerDate will count the number of commits per date of and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            System.out.println("\n    =countMergeCommitsPerDate will count the number of merge commits per date and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            System.out.println("\n    =countCommitsPerDateAndAuthor will count the number of commits of each member per date and will create an html page called 'visulog/results.html' with the results as a text.");
                            System.out.println("\n    =countMergeCommitsPerDateAndAuthor will count the number of merge commits of each member per date and will create an html page called 'visulog/results.html' with the results as a text.");
                            System.out.println("\n    =countLinesAddedPerFile will count the number of lines added in a file since the start and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            System.out.println("\n    =countLinesDeletedPerFile will count the number of lines deleted per file since the start and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            System.out.println("\n\n\n[--loadConfigFile] : this command will read the arguments/options of a file and execute them. \n    Syntaxe : --loadConfigFile=../[file name.txt]\n    Example : --loadConfigFile='../config.txt'");
                            System.out.println("\n\n\n[--justSaveConfigFile] : this command will create a file with a name and with a command starting by -- that will be saved in that file.\n    Syntaxe : [--plugin=argument] --justSaveConfigFile='../[name of the file.txt]'\n    Example : --addPlugin=countCommits --justSaveConfigFile='../config.txt'");
                            System.out.println("\n\n\n[--graphDisplay] : this command will change the boolean that will get the results or not as a graph to display in 'webgen/resultsGraph.html'. \n    Syntaxe : --graphDisplay=[true/false]\n    Example : --graphDisplay=true");
                            System.out.println("\n\n\n[--textDisplay] : this command will change the boolean that will get the results or not as a text to display in 'visulog/results.html'. \n    Syntaxe : --textDisplay=[true/false]\n    Example : --textDisplay=true");
                            System.out.println("\n\n\n[--githubUser] : this command will get all the informations about a gituser and show the result as a text. \n    Syntaxe : --githubUser=[Username]\n    Example : --githubUser=torvalds");
                            System.out.println("\n\n\n[--githubProjects] : this command will get all the informations about a github and will allow us to get the stats with --addPlugin , it will only work for 'countCommits' , 'countCommitsPerDay' , 'countCommitsPerDate' and 'countCommitsPerDateAndAuthor'. \n    Syntaxe : --githubProjects=[Username/ProjectName]\n    Example : --githubProjects=torvalds/linux");
                            }
                            
                            if(pValue.equals("addPlugin")){
                                System.out.println("[--addPlugin] : this command will run the analysis of the argument's plugin. \n    Syntaxe : --addPlugin=[Argument]\n    Example : --addPlugin=countCommits");
                                System.out.println("\n    =countCommits will count the number of commits of each member of the git and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                                System.out.println("\n    =countMergeCommits will count the number of merge commits of each member and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                                System.out.println("\n    =countCommitsPerDay will count the number of commits of each member per day and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                                System.out.println("\n    =countMergeCommitsPerDay will count the number of merge commits of each member per day and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a atext.");
                                System.out.println("\n    =countCommitsPerDate will count the number of commits per date of and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                                System.out.println("\n    =countMergeCommitsPerDate will count the number of merge commits per date and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                                System.out.println("\n    =countCommitsPerDateAndAuthor will count the number of commits of each member per date and will create an html page called 'visulog/results.html' with the results as a text.");
                                System.out.println("\n    =countMergeCommitsPerDateAndAuthor will count the number of merge commits of each member per date and will create an html page called 'visulog/results.html' with the results as a text.");
                                System.out.println("\n    =countLinesAddedPerFile will count the number of lines added in a file since the start and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                                System.out.println("\n    =countLinesDeletedPerFile will count the number of lines deleted per file since the start and will create an html page called 'webgen/resultsGraph.html' with the results as a graph , and 'visulog/results.html' as a text.");
                            }
                            if(pValue.equals("loadConfigFile")){
                                System.out.println("\n[--loadConfigFile] : this command will read the arguments/options of a file and execute them. \n    Syntaxe : --loadConfigFile=../[file name.txt]\n    Example : --loadConfigFile='../config.txt'");
                            }

                            if(pValue.equals("justSaveConfigFile")){
                                System.out.println("\n[--justSaveConfigFile] : this command will create a file with a name and with a command starting by -- that will be saved in that file.\n    Syntaxe : [--plugin=argument] --justSaveConfigFile='../[name of the file.txt]'\n    Example : --addPlugin=countCommits --justSaveConfigFile='../config.txt'");

                            }
                            if(pValue.equals("graphDisplay")){
                                System.out.println("\n[--graphDisplay] : this command will change the boolean that will get the results or not as a graph to display in 'webgen/resultsGraph.html'. \n    Syntaxe : --graphDisplay=[true/false]\n    Example : --graphDisplay=true");
                            }
                            if(pValue.equals("textDisplay")){
                                System.out.println("\n[--textDisplay] : this command will change the boolean that will get the results or not as a text to display in 'visulog/results.html'. \n    Syntaxe : --textDisplay=[true/false]\n    Example : --textDisplay=true");
                            }
                            if(pValue.equals("githubUser")){
                                System.out.println("\n[--githubUser] : this command will get all the informations about a gituser and show the result as a text. \n    Syntaxe : --githubUser=[Username]\n    Example : --githubUser=torvalds");
                            }
                            if(pValue.equals("githubProjects")){
                                System.out.println("\n[--githubProjects] : this command will get all the informations about a github and will allow us to get the stats with --addPlugin , it will only work for 'countCommits' , 'countCommitsPerDay' , 'countCommitsPerDate' and 'countCommitsPerDateAndAuthor'. \n    Syntaxe : --githubProjects=[Username/ProjectName]\n    Example : --githubProjects=torvalds/linux");
                            }
                            break; 
                        default:
                        return Optional.empty();
                    }
                }
            } else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        return Optional.of(new Configuration(gitPath, plugins,githubProjects,urlProject));
    }
    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        //TODO: print the list of options and their syntax
        System.exit(0);
    }
}
