package up.visulog.gitrawdata;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCommit {
    @Test
    public void testParseCommit() throws IOException, URISyntaxException {
        var expected = "Commit{id='6304c1acdc1cbdeb8315528781896abc72a021b8', date='Tue Sep 1 12:30:53 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='More gradle configuration (with subprojects)'}";
        var uri = getClass().getClassLoader().getResource("git.log").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var commit = Commit.parseCommit(reader);
            assertTrue(commit.isPresent());
            assertEquals(expected, commit.get().toString());
        }
    }

    @Test
    public void testParseLog() throws IOException, URISyntaxException {
        var expected = "[Commit{id='6304c1acdc1cbdeb8315528781896abc72a021b8', date='Tue Sep 1 12:30:53 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='More gradle configuration (with subprojects)'}, Commit{id='c0cf37d6b32897677e4b8f04be012e5379a7ab80', date='Thu Aug 27 23:49:03 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='first setup of project modules and gradle configuration'}, Commit{id='9e74f1581f23aaad21e2b936091d3ce371336e22', date='Mon Aug 31 11:28:28 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md - more modules'}, Commit{id='7484b0cb7b4e69e09c82ed38549750fa2a77f50c', date='Thu Aug 27 00:35:19 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md - translation...'}, Commit{id='9aaf6e09cc30909b32c68b4d5bf4ac50f95ecb93', date='Thu Aug 27 00:33:46 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md - some title left untranslated'}, Commit{id='969e2247156f27f27fec57b13faf6097bf4e2757', date='Thu Aug 27 00:32:47 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Update README.md -> in English, with some more details'}, Commit{id='486d76dbfd24ac65eeeeb16e57ae4fd68c8ecb1c', date='Thu Aug 27 00:02:55 2020 +0200', author='Aldric Degorre <adegorre@irif.fr>', description='Ajout de README.md avec définition des grandes lignes du sujet.'}]";
        var uri = getClass().getClassLoader().getResource("git.log").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var log = Commit.parseLog(reader);
//            System.out.println(log);
            assertEquals(expected, log.toString());
        }
    }

    @Test
    public void testConversionCalendar()
    {
        String date = "Wed Sep 29 20:33:07 2021 +0200";
        String[] parts = date.split(" ");
        int i = 0;
        for(i = 0; i < parts.length; i++)
        {
            System.out.println(parts[i]);

        }
        String month = parts[1];
        String day = parts[2];
        String year = parts[4];
        String[] time = parts[3].split(":");
        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        System.out.println(hour + "h "+ min+"m "+sec+"s" );



    }

    

    @Test
    public void TestJourMois()
    {
        String day = "Wed";
        String month = "Nov";
        System.out.println(day + " est "+Day.remplacer(day));
        System.out.println(month +" est "+Month.remplacer(month));

    }


}
