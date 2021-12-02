package up.visulog.gitrawdata;

import java.util.HashMap; 

public class Month {
    private static HashMap<String, String> monthsInFrench = new HashMap<String, String>();
    static{
        monthsInFrench.put("Jan", "Janvier");
        monthsInFrench.put("Feb", "Février");
        monthsInFrench.put("Mar", "Mars");
        monthsInFrench.put("Apr", "Avril");
        monthsInFrench.put("May", "Mai");
        monthsInFrench.put("Jun", "Juin");
        monthsInFrench.put("Jul", "Juillet");
        monthsInFrench.put("Aug", "Août");
        monthsInFrench.put("Sep", "Septembre");
        monthsInFrench.put("Oct", "Octobre");
        monthsInFrench.put("Nov", "Novembre");
        monthsInFrench.put("Dec", "Décembre");
    }

    private static HashMap<String, String> monthsInEnglish = new HashMap<String, String>();
    static{
        monthsInEnglish.put("Jan", "January");
        monthsInEnglish.put("Feb", "February");
        monthsInEnglish.put("Mar", "March");
        monthsInEnglish.put("Apr", "April");
        monthsInEnglish.put("May", "May");
        monthsInEnglish.put("Jun", "June");
        monthsInEnglish.put("Jul", "July");
        monthsInEnglish.put("Aug", "August");
        monthsInEnglish.put("Sep", "September");
        monthsInEnglish.put("Oct", "October");
        monthsInEnglish.put("Nov", "November");
        monthsInEnglish.put("Dec", "December");
    }

    private static HashMap<String, Integer> monthToInt = new HashMap<String, Integer>();
    static{
        monthToInt.put("Jan", 0);
        monthToInt.put("Feb", 1);
        monthToInt.put("Mar", 2);
        monthToInt.put("Apr", 3);
        monthToInt.put("May", 4);
        monthToInt.put("Jun", 5);
        monthToInt.put("Jul", 6);
        monthToInt.put("Aug", 7);
        monthToInt.put("Sep", 8);
        monthToInt.put("Oct", 9);
        monthToInt.put("Nov", 10);
        monthToInt.put("Dec", 11);
    }


    //Return the full month of the simplified month in French
    static String replaceFr(String s)
    {
        return monthsInFrench.get(s);
    }

    //Return the full month of the simplified month in English
    static String replaceEn(String s)
    {
        return monthsInEnglish.get(s);
    }

    //Return the int version of month 
    static int convertMonth(String s)
    {
        return monthToInt.get(s);
    }

    //renvoie 1 si month1 est superieur a month 2
    //-1 si month1 est inferieur a month2
    //0 sinon
    public static int estSuperieur(String month1, String month2){
        if(monthsTrie.get(month1)>monthsTrie.get(month2))
            return 1;
        if(monthsTrie.get(month1)<monthsTrie.get(month2))
            return -1;
        return 0;
    }

    private static HashMap<String, Integer> monthsTrie = new HashMap<String, Integer>();
    static{
        monthsTrie.put("January",1);
        monthsTrie.put("February",2);
        monthsTrie.put("March",3);
        monthsTrie.put("April",4);
        monthsTrie.put("May",5);
        monthsTrie.put("June",6);
        monthsTrie.put("July",7);
        monthsTrie.put("August",8);
        monthsTrie.put("September",9);
        monthsTrie.put("October",10);
        monthsTrie.put("November",11);
        monthsTrie.put("December",12);
    }
    
}
