package up.visulog.gitrawdata;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCommit {
    @Test
    public void testParseCommit() throws IOException, URISyntaxException {
        var expected = "Commit{id='6304c1acdc1cbdeb8315528781896abc72a021b8', date='Tuesday the 1st of September 2020 at 12h 30min 53sec', author='Aldric Degorre <adegorre@irif.fr>', description='More gradle configuration (with subprojects)'}";
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
        var expectedUTF8 = new String(expected.getBytes(), StandardCharsets.UTF_8);
        var uri = getClass().getClassLoader().getResource("git.log").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var log = Commit.parseLog(reader);
            System.out.println(log);
            assertEquals(expectedUTF8, log.toString());
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
        String dayString = parts[0];
        String month = parts[1];
        String day = parts[2];
        String year = parts[4];
        String[] time = parts[3].split(":");
        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        System.out.println(hour + "h "+ min+"m "+sec+"s" );
        StringBuilder sb = new StringBuilder();
        sb.append(Day.replaceFr(dayString));
        sb.append(" le");
        sb.append(" "+day);
        sb.append(" "+Month.replaceFr(month));
        sb.append(" "+year);



    }

    static String buildDateEnglish(String date)
    {
        // This is an example of date "Wed Sep 29 20:33:07 2021 +0200"
        //and this is an exemple of what we want to get "Saturday the 13th of April, 2019 at 20h 33min 07sec"
        String[] parts = date.split(" ");

        String dayString = parts[0];
        String month = parts[1];
        String day = parts[2];
        String year = parts[4];
        String[] time = parts[3].split(":");
        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        System.out.println(hour + "h "+ min+"m "+sec+"s" );
        StringBuilder sb = new StringBuilder();
        sb.append(Day.replaceEn(dayString));
        sb.append(" the");
        sb.append(" "+Day.addAfterDay(day));
        sb.append(" of");
        sb.append(" "+Month.replaceEn(month));
        sb.append(" "+year);
        sb.append(" at");
        sb.append(" "+hour+"h");
        sb.append(" "+min+"min");
        sb.append(" "+sec+"sec");
        
        return sb.toString();

    }

    

    @Test
    public void testJourMois()
    {
        String date  = "Thu Feb 23 20:33:07 1998 +0200";
        // String day = "Wed";
        // String month = "Nov";
        // String dayString = "23";
        // System.out.println(day + " est "+Day.replaceEn(day));
        // System.out.println(month +" est "+Month.replaceEn(month));
        // System.out.println(dayString +" est "+Day.addAfterDay(dayString));
        System.out.println(buildDateEnglish(date));

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");	
	    // Calendar calendar1 = new GregorianCalendar(2013,00,28,13,24,56);
        Calendar calendar2 = new GregorianCalendar(2013,01,27,13,24,56);

        // System.out.println(calendar1.compareTo(calendar2));
        // System.out.println(calendar2.compareTo(calendar1));
        System.out.println(calendar2.getTime());


        

    }

    @Test
    public void testCalendar()
    {
        String date  = "Thu Feb 23 20:33:07 1998 +0200";
        String[] parts = date.split(" ");

        String month = parts[1];
        String day = parts[2];
        String year = parts[4];
        String[] time = parts[3].split(":");
        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        Calendar calendar = new GregorianCalendar(Integer.parseInt(year),Month.convertMonth(month),
        Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(min),Integer.parseInt(sec));
        System.out.println(calendar.getTime());

    }


}
