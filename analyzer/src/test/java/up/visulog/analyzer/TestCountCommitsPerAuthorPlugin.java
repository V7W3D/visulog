package up.visulog.analyzer;

import org.junit.Test;
import up.visulog.gitrawdata.CommitBuilder;
import up.visulog.gitrawdata.Parsable;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import up.visulog.config.*;
import java.util.HashMap;

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
        Configuration config=new Configuration(Paths.get("."), new HashMap<String,PluginConfig>());
        var res = new CountCommitsPerAuthorPlugin(config).processLog(log);
        assertEquals(authors.length, res.getCommitsPerAuthor().size());
        var sum = res.getCommitsPerAuthor().values()
                .stream().reduce(0, Integer::sum);
        assertEquals(entries, sum.longValue());
    }

    @Test
    public void testCommitsPerDayAndAuthor(){
        CommitBuilder commit1=new CommitBuilder("dbd44b3ca23a58a7ff21e28c6e21d8d62ef7772");
        commit1.setAuthor("SIDI AHMED ahmed");
        commit1.setDate("Sat Nov 27 14:42:18 2021 +0100");
        commit1.setDescription("Code refactoring");
        commit1.setMergedFrom("066f216 0f38186");
        
        CommitBuilder commit2=new CommitBuilder("SIDI AHMED ahmed");
        commit2.setAuthor("SIDI AHMED ahmed");
        commit2.setDate("Sat Nov 27 14:42:18 2021 +0100");
        commit2.setDescription("Code refactoring");
        
        CommitBuilder commit3=new CommitBuilder("26c15e9b8ba34926c769fde56b9298b5ffb5399b");
        commit3.setAuthor("Mouloud Amara");
        commit3.setDate("Sat Nov 27 03:21:03 2021 +0100");
        commit3.setDescription("ajout plugin CountMergeCommitsPerDay");

        Parsable commit01=commit1.createCommit();
        Parsable commit02=commit2.createCommit();
        Parsable commit03=commit3.createCommit();

        List<Parsable> list=new LinkedList<Parsable>();
        list.add(commit01);
        list.add(commit02);
        list.add(commit03);
        Configuration config=new Configuration(Paths.get("."), new HashMap<String,PluginConfig>());
        CountCommitsPerDayAndAuthorPlugin.Result result=new CountCommitsPerDayAndAuthorPlugin(config).processLog(list);
        for(var res : result.getCommitsPerDayAndAuthor().entrySet()){
            System.out.println(res.getKey());
            for(var com : res.getValue().entrySet()){
                System.out.println(com.getKey() + " : "+com.getValue());
            }
        }

        
        
    }

    @Test
    public void testMergeCommitsPerDayAndAuthor(){
        CommitBuilder commit1=new CommitBuilder("dbd44b3ca23a58a7ff21e28c6e21d8d62ef7772");
        commit1.setAuthor("SIDI AHMED ahmed");
        commit1.setDate("Sat Nov 27 14:42:18 2021 +0100");
        commit1.setDescription("Code refactoring");
        commit1.setMergedFrom("066f218 0f88186");
        
        CommitBuilder commit2=new CommitBuilder("SIDI AHMED ahmed");
        commit2.setAuthor("SIDI AHMED ahmed");
        commit2.setDate("Sat Nov 27 14:42:18 2021 +0100");
        commit2.setDescription("Code refactoring");
        commit2.setMergedFrom("066f218 0f88186");
        
        CommitBuilder commit3=new CommitBuilder("26c15e9b8ba34926c769fde56b9298b5ffb5399b");
        commit3.setAuthor("Mouloud Amara");
        commit3.setDate("Sat Nov 27 03:21:03 2021 +0100");
        commit3.setDescription("ajout plugin CountMergeCommitsPerDay");
        commit3.setMergedFrom("055f547 0f88186");

        Parsable commit01=commit1.createCommit();
        Parsable commit02=commit2.createCommit();
        Parsable commit03=commit3.createCommit();

        List<Parsable> list=new LinkedList<Parsable>();
        list.add(commit01);
        list.add(commit02);
        list.add(commit03);

        Configuration config=new Configuration(Paths.get("."), new HashMap<String,PluginConfig>());
        CountMergeCommitsPerDayAndAuthorPlugin.Result result=new CountMergeCommitsPerDayAndAuthorPlugin(config).processLog(list);
        for(var res : result.getMergeCommitsPerDayAndAuthor().entrySet()){
            System.out.println(res.getKey());
            for(var com : res.getValue().entrySet()){
                System.out.println(com.getKey() + " : "+com.getValue());
            }
        }

        
        
    }
}
