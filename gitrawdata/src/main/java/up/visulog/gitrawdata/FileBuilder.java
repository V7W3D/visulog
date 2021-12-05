package up.visulog.gitrawdata;

public class FileBuilder {
    private String name;
    private int linesAdded;
    private int linesDeleted;

    public FileBuilder(String name, int linesAdded, int linesDeleted) {
        this.name = name;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
    }

    public void setLinesDeleted(int linesDeleted) {
        this.linesDeleted = linesDeleted;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    public void setName(String file) {
        this.name = file;
    }

    public File createFile(){
        return new File(name, linesAdded, linesDeleted);
    }
    
}
