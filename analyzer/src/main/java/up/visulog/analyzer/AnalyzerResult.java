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
            writer.write("<html><body>"+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc, cur) -> acc + cur) + "</body></html>");
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "Successfully creating the html file of the results.";
    }

}
