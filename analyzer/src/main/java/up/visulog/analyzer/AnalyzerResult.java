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
        //CreateFile();
        try {
            FileWriter writer = new FileWriter("../results.html");
           writer.write("<html>"+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc, cur) -> acc + cur) + "</body></html>");
            writer.close();

            System.out.println("Successfully wrote to the file.");

            FileWriter GrapheHTML = new FileWriter("../Graphedonnees.html");
            GrapheHTML.write("<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "  window.onload = function () {\n" +
                    "    var chart = new CanvasJS.Chart(\"chartContainer\",\n" +
                    "    {\n" +
                    "      title: {\n" +
                    "        text: \"Graphe commits par personne\"\n" +
                    "      },\n" +
                    "      data: [\n" +
                    "      {\n" +
                    "        type: \"column\",\n" +
                    "        dataPoints: [\n" +
                    "\n" +subResults.stream().map(AnalyzerPlugin.Result::getHTMLCommitAuth).reduce("", (acc, cur) -> acc + cur) +
                    "        ]\n" +
                    "      }\n" +
                    "      ]\n" +
                    "    });\n" +
                    "\n" +
                    "    chart.render();\n" +
                    "  }\n" +
                    "  </script>\n" +
                    "    <script type=\"text/javascript\" src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div id=\"chartContainer\" style=\"height: 300px; width: 100%;\">\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>");
            GrapheHTML.close();


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "Successfully creating the html file of the results.";
    }

}
