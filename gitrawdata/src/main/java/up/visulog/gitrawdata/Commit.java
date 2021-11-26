package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

public class Commit implements Parsable {
    // FIXME: (some of) these fields could have more specialized types than String
    // Modification de certains types par exemple id
    public final String id;
    public final String date;
    public final String author;
    public final String description;
    public final String mergedFrom;
    public Calendar calendarDate = Calendar.getInstance();

    public Commit(String id, String author, String date, String description, String mergedFrom) {
        this.id = id;
        this.author = author;
        this.date = buildDateEnglish(date);
        this.description = description;
        this.mergedFrom = mergedFrom;

    }

    //Return a better date format
    public String buildDateEnglish(String date)
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

        //We give value to the calendarDate field
        calendarDate = new GregorianCalendar(Integer.parseInt(year),Month.convertMonth(month),
        Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(min),Integer.parseInt(sec));

        return sb.toString();

    }


    // TODO: factor this out (similar code will have to be used for all git commands)
    public static List<Commit> parseLogFromCommand(Path gitPath) {
        ProcessBuilder builder =
                new ProcessBuilder("git", "log").directory(gitPath.toFile());
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error running \"git log\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return parseLog(reader);
    }

    public static List<Commit> parseLog(BufferedReader reader) {
        var result = new ArrayList<Commit>();
        Optional<Commit> commit = parseCommit(reader);
        while (commit.isPresent()) {
            result.add(commit.get());
            commit = parseCommit(reader);
        }
        return result;
    }

    /**
     * Parses a log item and outputs a commit object. Exceptions will be thrown in case the input does not have the proper format.
     * Returns an empty optional if there is nothing to parse anymore.
     */
    public static Optional<Commit> parseCommit(BufferedReader input) {
        try {

            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            var idChunks = line.split(" ");
            if (!idChunks[0].equals("commit")) parseError();
            var builder = new CommitBuilder(idChunks[1]);

            line = input.readLine();
            while (!line.isEmpty()) {
                var colonPos = line.indexOf(":");
                var fieldName = line.substring(0, colonPos);
                var fieldContent = line.substring(colonPos + 1).trim();
                switch (fieldName) {
                    case "Author":
                        builder.setAuthor(fieldContent);
                        break;
                    case "Merge":
                        builder.setMergedFrom(fieldContent);
                        break;
                    case "Date":
                        builder.setDate(fieldContent);
                        break;
                    default: // TODO: warn the user that some field was ignored
                }
                line = input.readLine(); //prepare next iteration
                if (line == null) parseError(); // end of stream is not supposed to happen now (commit data incomplete)
            }

            // now read the commit message per se
            var description = input
                    .lines() // get a stream of lines to work with
                    .takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
                    .map(String::trim) // remove indentation
                    .reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            builder.setDescription(description);
            return Optional.of(builder.createCommit());
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }

    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong commit format.");
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                (mergedFrom != null ? ("mergedFrom...='" + mergedFrom + '\'') : "") + //TODO: find out if this is the only optional field
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    
}
