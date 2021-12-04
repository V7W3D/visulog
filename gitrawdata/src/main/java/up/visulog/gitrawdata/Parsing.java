package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parsing {
    public static List<Parsable> parseLogFromCommand(Path gitPath, ArrayList<String> command) {
        ProcessBuilder builder =
                new ProcessBuilder(command).directory(gitPath.toFile());
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error running \"git command\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        switch(command.get(1)) {
            case "log":
                return Commit.parseLog(reader);
            default: 
                return null;
        }
    }
}
