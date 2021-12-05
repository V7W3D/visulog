package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class File implements Parsable {
    public final String name;
    public final int linesAdded;
    public final int linesDeleted;
    
    public File(String name, int linesAdded, int linesDeleted) {
        this.name = name;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
    }

    public static List<Parsable> parseLog(BufferedReader reader) {
        var result = new ArrayList<Parsable>();
        Optional<File> file = parseFile(reader);
        while (file.isPresent()) {
            result.add(file.get());
            file = parseFile(reader);
        }
        return result;
    }

    private static void parseError() {
        throw new RuntimeException("Wrong format.");
    }

    public static Optional<File> parseFile(BufferedReader input) {
        try {

            var line = input.readLine();
            if (line == null) return Optional.empty();
            while(line.charAt(0) == '-'){
               line = input.readLine();
               if (line == null) return Optional.empty();
            }
            var chunks = line.split("\\t");
            if(chunks.length == 3){
                var linesAdded = chunks[0];
                var linesDeleted = chunks[1];
                var name = chunks[2];
                var builder = new FileBuilder(name, Integer.parseInt(linesAdded), Integer.parseInt(linesDeleted));
                return Optional.of(builder.createFile());
            }
            else parseError();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
