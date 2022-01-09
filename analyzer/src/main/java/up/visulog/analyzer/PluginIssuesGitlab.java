
package up.visulog.analyzer;

        import up.visulog.config.Configuration;
        import up.visulog.gitrawdata.Commit;
        import up.visulog.gitrawdata.GithubCommit;
        import up.visulog.gitrawdata.Parsable;
        import up.visulog.gitrawdata.Parsing;

        import java.io.*;
        import java.net.URL;
        import java.net.URLConnection;
        import java.util.*;

public class PluginIssuesGitlab extends AnalyzerGitLogPlugin {
    public static int nbcommits=0;

    public PluginIssuesGitlab(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    protected Result processLog(List<Parsable> gitLog) {
        var result = new Result();
        for (var parsable : gitLog) {
            Commit commit = (Commit) parsable;
            var nb = result.commits.getOrDefault(commit.author, 0);
            result.commits.put(commit.author, nb + 1);

            var nb1 = result.ListecomMerged.getOrDefault(commit.mergedFrom, 0);
            result.ListecomMerged.put(commit.mergedFrom, nb + 1);
        }
        return result;
    }

    @Override
    public void run() {
        if(!configuration.githubOrNormal()){
            if(listCommits==null)
                result = processLog(Parsing.parseLogFromCommand(configuration.getGitPath(),configuration.buildCommand("Issues")));
            else
                result = processLog(listCommits);
        }else{
            result=processLog(GithubCommit.getGithubCommits(configuration.getUrlProject()));
        }
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commits = new HashMap<>();
        private final Map<String, Integer> ListecomMerged= new HashMap<>();

        Map<String, Integer> getCommitsdate() {
            return ListecomMerged;
        }

        Map<String, Integer> getCommitsPerAuth() {
            return commits;
        }

        @Override
        public String getResultAsString() {
            return ListecomMerged.toString();
        }

        @Override
        public String getResultAsDataPoints() {
            StringBuilder dataPoints = new StringBuilder();

            return "";
        }



        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Liste des issues:  <ul>");
            ///////////////

            File fw =new File("../IssuesHTML.html");
            BufferedWriter bf=null;
            try {
                URL url = new URL("https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/3327/issues");

                // Get the input stream through URL Connection
                URLConnection con = url.openConnection();
                InputStream is =con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line = null;
                while ((line = br.readLine()) != null) {


                    bf=new BufferedWriter(new FileWriter(fw));
                    bf.write("<!DOCTYPE html>\n" +
                            "<html lang=\"en-US\">\n" +
                            "\n" +
                            "<head>\n" +
                            "\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                            "\n" +
                            "    <title></title>\n" +
                            "\n" +
                            "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" +
                            "\n" +
                            "    <script>\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "$.getJSON = function(url, callbackFunction) {\n" +
                            "  var jsonFetchedOn2017_12_21 = {\n" +
                            "    \"results\":"+line+"};\n" +
                            "  callbackFunction(jsonFetchedOn2017_12_21);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "function runAfterDocumentLoads() {\n" +
                            "  causeButtonClicksToLoadJSONData();\n" +
                            "}\n" +
                            "\n" +
                            "function causeButtonClicksToLoadJSONData() {\n" +
                            "  var button = $(\"button.zip\");\n" +
                            "  button.click(loadJSONData);\n" +
                            "}\n" +
                            "function loadJSONData() {\n" +
                            "  var jQuery = $;\n" +
                            "  var json_url = \"https://whoismyrepresentative.com/getall_mems.php?zip=31023&output=json\";\n" +
                            "  jQuery.getJSON(json_url, addJsonToPage);\n" +
                            "}\n" +
                            "\n" +
                            "function addJsonToPage(jsonResults) {\n" +
                            "  var jQuery = $;\n" +
                            "  var representativeList = extractRepresentativeFromJsonResults(jsonResults);\n" +
                            "  jQuery.each(representativeList, addRepresentativeToPage);\n" +
                            "}\n" +
                            "\n" +
                            "function extractRepresentativeFromJsonResults(jsonObject) {\n" +
                            "  return jsonObject.results;\n" +
                            "}\n" +
                            "function addRepresentativeToPage(arrayIndex, aRepresentative) {\n" +
                            "  var divElementCollection = $(\"div.rep\");\n" +
                            "  var issueDes = \"<div class=\\'title\\'>\"+ aRepresentative.title +\"</div>\";\n" +
                            "  var Dateissue = \"<li>Created Date :</li> \"+ aRepresentative.created_at + \"\";\n" +
                            "  var State = \"<li>State :</li>\" + aRepresentative.state + \"\";\n" +
                            "  var CloseIssue = \"<li>Closed Date :</li>\" + aRepresentative.closed_at + \"\";\n" +
                            "  divElementCollection.append(issueDes);\n" +
                            "  divElementCollection.append(Dateissue);\n" +
                            "  divElementCollection.append(State);\n" +
                            "  divElementCollection.append(CloseIssue);\n" +
                            "}\n" +
                            "\n" +
                            "$(document).ready(runAfterDocumentLoads);\n" +
                            "\n" +
                            "</script>\n" +
                            "\n" +
                            "</head>\n" +
                            "\n" +
                            "<link rel=\"stylesheet\" href=\"style1.css\">"+
                            "<body>\n" +
                            "\n" +
                            "<button class=\"zip\">\n" +
                            "\n" +
                            "    Liste issues\n" +
                            "\n" +
                            "</button>\n" +
                            "\n" +
                            "<div class=\"rep\">\n" +
                            "\n" +
                            "\n" +
                            "</div>\n" +
                            "\n" +
                            "</body>\n" +
                            "</html>");
                    bf.newLine();
                    bf.flush();}
            }catch (IOException e){
                System.out.println("erreur");

            }

            return html.toString();
        }

        @Override
        public String getChartName() {
            return "List commits merged to develop";
        }
    }
}

