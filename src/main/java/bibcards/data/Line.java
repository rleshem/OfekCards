package bibcards.data;

import bibcards.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/*
כרטיס-גנזים 109:
ש: בית חולים צבאי
ה: שיר
מ: דבר
ל: 10.5.57 (אולי 51)
ד: עמ 3
 */
public abstract class Line {

    private final LineType type;
    protected String prefix;
    protected String content;

    public enum LineType {
        NAME,       // yes: ש (שם)
        REMARK,     // yes: ה (הערה)
        SOURCE,     // yes: מ (מקור)
        HEB_DATE,   // yes: ע (עברי)
        GREG_DATE,  // yes: ל (לועזי)
        PAGE,       // yes: ד (דף)
        REF_KIND,   // ס (סוג: מחבר, תרגום, ...)
        PSEUDONYM,  // yes: ט (שם עט)
        SUB_TITLE,  // yes: ת (תת כותרת)
        IMPORTANCE, // yes: ח (חשיבות)
        REPORTER,   // yes: ר (רשם, כתב)
        CARD        // yes: כ (כרטיס-גנזים)
    }

    /*
    private static Map<String, LineType> typesByKey = new HashMap<String, LineType>() {{
        put("ש", LineType.Name);
        put("ה", LineType.Remark);
    }};
     */
    private static Map<String, Supplier<Line>> typeSupplierMap = new HashMap<String, Supplier<Line>>() {{
        put("ש", NameLine::new);
        put("ה", RemarkLine::new);
        put("מ", SourceLine::new);
        put("ע", HebDateLine::new);
        put("ל", GregDateLine::new);
        put("ד", PageLine::new);
        put("ט", PseudonymLine::new);
        put("ת", SubTitleLine::new);
        put("ח", ImportanceLine::new);
        put("ר", ReporterLine::new);
        put("כ", CardLine::new);
    }};

    public static Line createLineOf(String kind, int numLines) {
        Supplier<Line> supplier = typeSupplierMap.get(kind);
        if (supplier == null) {
            System.err.println("Unknown line type <" + kind + "> - input line=" + numLines);
            System.exit(-1);
        }
        return supplier.get();
    }

    public static Line genLine(LineType type) {
        switch (type) {
            case PSEUDONYM:
                return new PseudonymLine();
            default:
                throw new IllegalArgumentException("cannot generate data type <" + type + ">");
        }
    }

    public Line(LineType type) {
        this.type = type;
    }

    public LineType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    // ש: א. שמאלי באור חדש ; ויהי המבול על הארץ החלוצים
    public void setContent(String inputLine) {
        if (!inputLine.contains(":"))
            Logger.error("no : in input line");
        this.prefix = inputLine.substring(0, inputLine.indexOf(':'));
        /*
            skip the prefix part, potentially with digit:
            ש: א. שמאלי באור חדש
            ש1: א. שמאלי באור חדש
         */
        int dividerPos = inputLine.indexOf(":") + 1;
        // skip spaces after ":" - if exist
        boolean keepLooking = true;
        while (keepLooking) {
            if (inputLine.length() <= dividerPos) {
                Logger.error("no content in line <" + inputLine + ">");
                this.content = "~ no content in line ~";
                return;
            }
            if (inputLine.charAt(dividerPos) == ' ')
                dividerPos++;
            else
                keepLooking = false;
        }
        this.content = inputLine.substring(dividerPos);
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isCanBeMulti() { return false; }

}
