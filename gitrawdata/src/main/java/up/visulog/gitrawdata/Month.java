package up.visulog.gitrawdata;

import java.util.Map;
import static java.util.Map.entry;

import java.util.HashMap; 

public class Month {
    private static HashMap<String, String> months = new HashMap<String, String>();
    static{
        months.put("Jan", "Janvier");
        months.put("Feb", "Février");
        months.put("Mar", "Mars");
        months.put("Apr", "Avril");
        months.put("May", "Mai");
        months.put("Jun", "Juin");
        months.put("Jul", "Juillet");
        months.put("Aug", "Août");
        months.put("Sep", "Septembre");
        months.put("Oct", "Octobre");
        months.put("Nov", "Novembre");
        months.put("Dec", "Décembre");
    }

    static String remplacer(String s)
    {
        return months.get(s);
    }
    
}
