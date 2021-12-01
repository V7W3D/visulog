package up.visulog.analyzer;

import java.io.IOException;

import org.json.JSONException;

public interface AnalyzerPlugin {
    interface Result {
        String getResultAsString();
        String getResultAsHtmlDiv();
        // Methode define pour recuperer le nombre de commits par date
        String getResultAsStringdate();
    }

    /**
     * run this analyzer plugin
     * @throws IOException
     * @throws JSONException
     *
     *
     */
    void run() throws JSONException, IOException;

    /**
     *
     * @return the result of this analysis. Runs the analysis first if not already done.
     * @throws IOException
     * @throws JSONException
     */
    Result getResult() throws JSONException, IOException;
}
