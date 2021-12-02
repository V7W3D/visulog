package up.visulog.analyzer;

public interface AnalyzerPlugin {
    interface Result {
        String getResultAsString();
        String getResultAsHtmlDiv();
        public String getResultAsStringdate();
        public String getResultMergeRequest();
        public String getHTMLCommitAuth();
    }

    /**
     * run this analyzer plugin
     */
    void run();

    /**
     *
     * @return the result of this analysis. Runs the analysis first if not already done.
     */
    Result getResult();
}
