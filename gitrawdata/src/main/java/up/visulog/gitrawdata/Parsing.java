package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Parsing {
    public static List<Commit> parseLogFromCommand(Path gitPath, List<String> command) {//On rajoute une liste d'arguments comme paramètre
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

    public static List<Commit> parseLog(BufferedReader reader) {
        var result = new ArrayList<Commit>();//Il se peut qu'on récupère autre chose que des commits / On pense à créer une classe mère de tout les types de données qui peuvent être parsé
        return result;
    }

    
}
