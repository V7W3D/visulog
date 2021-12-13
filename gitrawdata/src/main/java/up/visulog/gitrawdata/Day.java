package up.visulog.gitrawdata;

import java.util.HashMap;
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

    public static HashMap<Integer, String> dayTrie = new HashMap<>();
    static{
        dayTrie.put(0,"Monday");
        dayTrie.put(1,"Tuesday");
        dayTrie.put(2,"Wednesday");
        dayTrie.put(3,"Thursday");
        dayTrie.put(4,"Friday");
        dayTrie.put(5,"Saturday");
        dayTrie.put(6,"Sunday");
    }

    private static Map<Integer, String> intToDay = Map.of(
        1, "Sun",    
        2, "Mon",
        3, "Tue",
        4, "Wed",
        5, "Thu",
        6, "Fri",
        7, "Sat"          
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

    static String convertIntToDay(int i){
        return intToDay.get(i);
    }
    
}
