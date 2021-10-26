package up.visulog.gitrawdata;

import java.util.Map;

public class Day {
    private static Map<String, String> days = Map.of(
        "Mon", "Lundi",
        "Tue", "Mardi",
        "Wed", "Mercredi",
        "Thu", "Jeudi",
        "Fri", "Vendredi",
        "Sat", "Samedi",
        "Sun", "Dimanche"
    );

    static String remplacer(String s)
    {
        return days.get(s);
    }
    
}
