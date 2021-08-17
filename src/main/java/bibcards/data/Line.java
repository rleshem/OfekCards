package bibcards.data;

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
        put("כ", CardLine::new);
    }};

    public static Line createLineOf(String kind) {
        Supplier<Line> supplier = typeSupplierMap.get(kind);
        if (supplier == null) {
            System.err.println("Unknown line type <" + kind + ">");
            return null;
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

    private final LineType type;
    protected String content;

    public Line(LineType type) {
        this.type = type;
    }

    public LineType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public boolean isCanBeMulti() { return false; }

}
