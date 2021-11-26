package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;

public class Parsing {
    public static List<Parsable> parseLogFromCommand(Path gitPath, List<String> command) {//On rajoute une liste d'arguments comme paramètre
        ProcessBuilder builder =
                new ProcessBuilder(command).directory(gitPath.toFile());//on passe tous les pramètres de la liste
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error running \"git command\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return parseLog(reader);//La partie parsing the log diffère suivant l'output de la commande éxécutée
    }

    public static List<Parsable> parseLog(BufferedReader reader) {
        return null;
    }

    
}