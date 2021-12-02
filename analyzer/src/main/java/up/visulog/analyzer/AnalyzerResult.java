package up.visulog.analyzer;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class AnalyzerResult {
    public List<AnalyzerPlugin.Result> getSubResults() {
        return subResults;
    }

    private final List<AnalyzerPlugin.Result> subResults;

    public AnalyzerResult(List<AnalyzerPlugin.Result> subResults) {
        this.subResults = subResults;
    }

    @Override
    public String toString() {
        return subResults.stream().map(AnalyzerPlugin.Result::getResultAsString).reduce("", (acc, cur) -> acc + "\n" + cur);
    }
    public static void CreateFile(){
        try {
            File fic = new File("../results.html");
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
    public String toHTML() {
        CreateFile();
        try {
            FileWriter writer = new FileWriter("../results.html");
            writer.write("<html><head><script> window.onload = function () { var chart = new CanvasJS.Chart('chartContainer', { animationEnabled: true, exportEnabled: true, theme: 'light1', title:{ text: 'Simple Column Chart with Index Labels' }, axisY: { includeZero: true }, data: [{ type: 'column', indexLabelFontColor: '#5A5757', indexLabelFontSize: 16, indexLabelPlacement: 'outside', dataPoints: ["+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc, cur) -> acc + cur) + "]}]});chart.render();}</script></head>");
            writer.write("<body><div id='chartContainer' style='height: 300px; width: 100%;'></div><script src='https://canvasjs.com/assets/script/canvasjs.min.js'></script></body></html>");
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "Successfully creating the html file of the results.";
    }

}
