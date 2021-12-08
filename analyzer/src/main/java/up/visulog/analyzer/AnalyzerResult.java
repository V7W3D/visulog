package up.visulog.analyzer;

import java.util.Iterator;
import java.util.List;

import up.visulog.analyzer.AnalyzerPlugin.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AnalyzerResult {
    public List<AnalyzerPlugin.Result> getSubResults() {
        return subResults;
    }

    private final List<AnalyzerPlugin.Result> subResults;
    private final Iterator<AnalyzerPlugin.Result> it;

    public AnalyzerResult(List<AnalyzerPlugin.Result> subResults) {
        this.subResults = subResults;
        it = subResults.iterator();
    }

    @Override
    public String toString() {
        return subResults.stream().map(AnalyzerPlugin.Result::getResultAsString).reduce("", (acc, cur) -> acc + "\n" + cur);
    }
    public static void createFile(String name){
        try {
            File fic = new File(name);
            if (fic.createNewFile()) {
                System.out.println("File created: " + fic.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public String toHTMLGraph() {
        createFile("../webgen/resultsGraph.html");
        int i = 0;
        StringBuilder chartContainers = new StringBuilder();
        StringBuilder charts = new StringBuilder(); 
        StringBuilder rendering = new StringBuilder();
        while(it.hasNext()) {
            Result result = it.next();
            if(! (result instanceof CountCommitsPerDateAndAuthorPlugin.Result || result instanceof CountMergeCommitsPerDateAndAuthorPlugin.Result)){
                try {
                    String chartContainer = readFile("../webgen/chartContainer.html", Charset.forName("UTF-8"));
                    chartContainer = chartContainer.replace("chartContainer","chartContainer" + i);
                    chartContainers.append(chartContainer);
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                try {
                    String chart = readFile("../webgen/chart.js",Charset.forName("UTF-8"));
                    chart = chart.replace("/*data*/",result.getResultAsDataPoints());
                    chart = chart.replace("myData","myData" + i);
                    chart = chart.replace("myConfig","myConfig"+ i);
                    chart = chart.replace("chartContainer","chartContainer" + i);
                    chart = chart.replace("myChart","myChart" + i);
                    chart = chart.replace("/*title*/",result.getChartName());                
                    charts.append(chart);
                    rendering.append("myChart"+i+".render();");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        
        String squelette = null;
        try {
            squelette = readFile("../webgen/squelette.html",Charset.forName("UTF-8"));
            squelette = squelette.replace("/*charts*/",charts.toString());
            squelette = squelette.replace("<!--div-->",chartContainers.toString());
            squelette = squelette.replace("/*rendering*/",rendering.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter("../webgen/resultsGraph.html");
            writer.write(squelette);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Successfully creating the html file of graphs of the results.";
    }

    public String toHTML(){
        createFile("../results.html");
        try{
            FileWriter writer=new FileWriter("../results.html");
            writer.write("<html><body>"+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc,cur) ->acc+cur)+"<body><html>");
            writer.close();
            System.out.println("Successfully wrote to the file");
        }catch(IOException e){
            System.out.println("An error occured");
            e.printStackTrace();
        }
        return "Successfully creating the html file of the results.";
    }
}
