package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Optional;

public class CLILauncher {

    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
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
                            switch(pValue) {
                                case "countCommits" : 
                                    plugins.put("countCommits", new PluginConfig(){});
                                    break;
                                case "countMergeCommits" : 
                                    plugins.put("countMergeCommits", new PluginConfig(){});
                                    break;
                                case "countMergeCommitsPerDay" :
                                    plugins.put("countMergeCommitsPerDay", new PluginConfig(){});
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
                        default:
                            return Optional.empty();
                    }
                }
            } else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        return Optional.of(new Configuration(gitPath, plugins));
    }

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        //TODO: print the list of options and their syntax
        System.exit(0);
    }
}
