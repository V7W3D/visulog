package up.visulog.gitrawdata;

import java.util.Map;

public class Day {
    private static Map<String, String> daysInFrench = Map.of(
        "Mon", "Lundi",
        "Tue", "Mardi",
        "Wed", "Mercredi",
        "Thu", "Jeudi",
        "Fri", "Vendredi",
        "Sat", "Samedi",
        "Sun", "Dimanche"
    );

    private static Map<String, String> daysInEnglish = Map.of(
        "Mon", "Monday",
        "Tue", "Tuesday",
        "Wed", "Wednesday",
        "Thu", "Thursday",
        "Fri", "Friday",
        "Sat", "Saturday",
        "Sun", "Sunday"
    );

    private static Map<String, String> afterDay = Map.of(
        "0", "th",
        "1", "st",
        "2", "nd",
        "3", "rd",
        "4", "th",
        "5", "th",
        "6", "th",
        "7", "th",
        "8", "th",
        "9", "th"
    );


    //Return the full day of the simplified day in French
    static String replaceFr(String s)
    {
        return daysInFrench.get(s);
    }

    //Return the full day of the simplified day in English
    static String replaceEn(String s)
    {
        return daysInEnglish.get(s);
    }

    //Return the day number and the string usually after
    static String addAfterDay(String s)
    {
        return s+afterDay.get(s.substring(s.length()-1));
    }
    
}
