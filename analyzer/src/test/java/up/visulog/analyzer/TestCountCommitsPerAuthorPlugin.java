package up.visulog.analyzer;

import org.junit.Test;
import up.visulog.gitrawdata.CommitBuilder;
import up.visulog.gitrawdata.Parsable;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestCountCommitsPerAuthorPlugin {
    /* Let's check whether the number of authors is preserved and that the sum of the commits of each author is equal to the total number of commits */
    @Test
    public void checkCommitSum() {
        var log = new ArrayList<Parsable>();
        String[] authors = {"foo", "bar", "baz"};
        var entries = 20;
        for (int i = 0; i < entries; i++) {
            log.add(new CommitBuilder("").setAuthor(authors[i % 3]).setDate("Wed Sep 29 20:33:07 2021 +0200").createCommit());
        }
        var res = CountCommitsPerAuthorPlugin.processLog(log);
        assertEquals(authors.length, res.getCommitsPerAuthor().size());
        var sum = res.getCommitsPerAuthor().values()
                .stream().reduce(0, Integer::sum);
        assertEquals(entries, sum.longValue());
    }
}
