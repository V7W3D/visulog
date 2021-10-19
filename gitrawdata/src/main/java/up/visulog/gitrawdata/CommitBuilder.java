package up.visulog.gitrawdata;

import java.util.Calendar;

public class CommitBuilder {
    private final String id;
    private String author;
    private Calendar date;
    private String description;
    private String mergedFrom;

    public CommitBuilder(String id) {
        this.id = id;
    }

    public CommitBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public CommitBuilder setDate(Calendar date) {
        this.date = date;
        return this;
    }

    public CommitBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public CommitBuilder setMergedFrom(String mergedFrom) {
        this.mergedFrom = mergedFrom;
        return this;
    }

    public Commit createCommit() {
        return new Commit(id, author, date, description, mergedFrom);
    }

    public String dateToString()
    {
        return "";
    }
}