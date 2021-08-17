package bibcards.data;

import bibcards.util.Logger;
import bibcards.util.Setup;

import java.util.HashMap;
import java.util.Map;

public class PreFixer {

    /*
        public static Map<String, Integer> fieldSldBits = new LinkedHashMap<String, Integer>() {{
            put(FIELD_SLD__UUID, 1);

     */

    /*



    טבלה תחיליות

שם
ש1
ש2...
שם הפריט
הערה
הערה (מרובות)
מקור
עיתון \ פרסום
עברי
תאריך עברי
לועזי
תאריך לועזי
דף
עמוד \ דף
סוג
מחבר, תרגום, ...
ט
שם עט
ת
ת1...
תת כותרת
ח
חשיבות
ר
מחבר הכתבה

אפשרויות פריטים מרובים באותו כרטיס:
ע \ ל \ ד
ע1 \ ל1 \ ד1

     */
    private static Map<String, String> namedPrefixes = new HashMap<String, String>() {{
        put("ה", "Comment");
        put("ד", "Page");
    }};

    public static String getName(String key) {
        if (namedPrefixes.keySet().contains(key))
            return namedPrefixes.get(key);
        else
            return ("NO VALUE FOR <" + key + ">");
    }

    public static boolean isKey(String key) {
        return namedPrefixes.keySet().contains(key);
    }

    @Deprecated
    public static void loadPrefixes() {
        String prefixes = Setup.getSetup().getStringProperty(Setup.prefixes);
        String[] parts = prefixes.split(";");
        for (String prefix : parts) {
            String[] single = prefix.split("=");
            Logger.log(4, "single: <" + single + ">");
        }
    }

    public static void main(String[] args) {
        int i = 0;
        for (String key : namedPrefixes.keySet()) {
            System.out.println(i++ + " : " + key + " : " + namedPrefixes.get(key));
        }
    }
}
